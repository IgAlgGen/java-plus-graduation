package ru.practicum.ewm.stats.analyzer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.analyzer.model.EventSimilarityEntity;
import ru.practicum.ewm.stats.analyzer.model.EventSimilarityId;
import ru.practicum.ewm.stats.analyzer.model.UserEventInteractionEntity;
import ru.practicum.ewm.stats.analyzer.model.UserEventInteractionId;
import ru.practicum.ewm.stats.analyzer.repository.EventSimilarityRepository;
import ru.practicum.ewm.stats.analyzer.repository.UserEventInteractionRepository;
import ru.practicum.ewm.stats.avro.ActionWeight;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AnalyzerServiceImpl implements AnalyzerService {

    private static final int RECENT_INTERACTIONS_LIMIT = 100;
    private static final int NEIGHBORS_LIMIT = 20;

    private final UserEventInteractionRepository interactionRepository;
    private final EventSimilarityRepository similarityRepository;

    public AnalyzerServiceImpl(UserEventInteractionRepository interactionRepository,
                               EventSimilarityRepository similarityRepository) {
        this.interactionRepository = interactionRepository;
        this.similarityRepository = similarityRepository;
    }

    @Override
    @Transactional
    public void updateUserAction(UserActionAvro userAction) {
        UserEventInteractionId id = new UserEventInteractionId(userAction.getUserId(), userAction.getEventId());
        double weight = ActionWeight.getWeight(userAction.getActionType());
        Instant timestamp = userAction.getTimestamp();

        UserEventInteractionEntity interaction = interactionRepository.findById(id)
                .orElseGet(() -> new UserEventInteractionEntity(id, weight, timestamp));

        if (weight > interaction.getWeight()) {
            interaction.setWeight(weight);
        }
        if (timestamp.isAfter(interaction.getUpdatedAt())) {
            interaction.setUpdatedAt(timestamp);
        }

        interactionRepository.save(interaction);
    }

    @Override
    @Transactional
    public void updateEventSimilarity(EventSimilarityAvro eventSimilarity) {
        EventSimilarityId id = new EventSimilarityId(eventSimilarity.getEventA(), eventSimilarity.getEventB());
        Instant timestamp = eventSimilarity.getTimestamp();

        EventSimilarityEntity similarity = similarityRepository.findById(id)
                .orElseGet(() -> new EventSimilarityEntity(id, eventSimilarity.getScore(), timestamp));

        similarity.setScore(eventSimilarity.getScore());
        similarity.setUpdatedAt(timestamp);

        similarityRepository.save(similarity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecommendedEvent> getSimilarEvents(long eventId, long userId, int maxResults) {
        if (maxResults <= 0) {
            return List.of();
        }

        Set<Long> interactedEventIds = getInteractedEventIds(userId);

        return similarityRepository.findAllByEvent(eventId).stream()
                .map(similarity -> new RecommendedEvent(similarity.getOtherEvent(eventId), similarity.getScore()))
                .filter(recommendation -> !interactedEventIds.contains(recommendation.eventId()))
                .limit(maxResults)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecommendedEvent> getRecommendationsForUser(long userId, int maxResults) {
        if (maxResults <= 0) {
            return List.of();
        }

        List<UserEventInteractionEntity> history = interactionRepository.findByIdUserIdOrderByUpdatedAtDesc(userId);
        if (history.isEmpty()) {
            return List.of();
        }

        Set<Long> interactedEventIds = history.stream()
                .map(interaction -> interaction.getId().getEventId())
                .collect(Collectors.toSet());

        List<Long> recentEventIds = history.stream()
                .limit(RECENT_INTERACTIONS_LIMIT)
                .map(interaction -> interaction.getId().getEventId())
                .toList();

        Map<Long, Double> candidates = findCandidateEvents(recentEventIds, interactedEventIds);
        if (candidates.isEmpty()) {
            return List.of();
        }

        return candidates.keySet().stream()
                .map(candidateId -> predictScore(candidateId, history))
                .filter(recommendation -> recommendation.score() > 0.0)
                .sorted(Comparator.comparingDouble(RecommendedEvent::score).reversed())
                .limit(maxResults)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecommendedEvent> getInteractionsCount(Collection<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return List.of();
        }

        Map<Long, Double> counts = new HashMap<>();
        for (Object[] row : interactionRepository.sumWeightsByEventIds(eventIds)) {
            Long eventId = (Long) row[0];
            Number score = (Number) row[1];
            counts.put(eventId, score.doubleValue());
        }

        return eventIds.stream()
                .distinct()
                .map(eventId -> new RecommendedEvent(eventId, counts.getOrDefault(eventId, 0.0)))
                .toList();
    }

    private Set<Long> getInteractedEventIds(long userId) {
        return interactionRepository.findByIdUserIdOrderByUpdatedAtDesc(userId).stream()
                .map(interaction -> interaction.getId().getEventId())
                .collect(Collectors.toSet());
    }

    private Map<Long, Double> findCandidateEvents(List<Long> recentEventIds, Set<Long> interactedEventIds) {
        if (recentEventIds.isEmpty()) {
            return Map.of();
        }

        Map<Long, Double> candidates = new HashMap<>();
        for (EventSimilarityEntity similarity : similarityRepository.findAllByAnyEventIn(recentEventIds)) {
            Long first = similarity.getId().getEventA();
            Long second = similarity.getId().getEventB();

            if (recentEventIds.contains(first) && !interactedEventIds.contains(second)) {
                candidates.merge(second, similarity.getScore(), Math::max);
            }
            if (recentEventIds.contains(second) && !interactedEventIds.contains(first)) {
                candidates.merge(first, similarity.getScore(), Math::max);
            }
        }

        return candidates.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    private RecommendedEvent predictScore(long candidateId, List<UserEventInteractionEntity> history) {
        Set<Long> historyIds = history.stream()
                .map(interaction -> interaction.getId().getEventId())
                .collect(Collectors.toCollection(HashSet::new));

        Map<Long, Double> userWeights = history.stream()
                .collect(Collectors.toMap(
                        interaction -> interaction.getId().getEventId(),
                        UserEventInteractionEntity::getWeight,
                        Math::max
                ));

        List<RecommendedEvent> neighbors = new ArrayList<>();
        for (EventSimilarityEntity similarity : similarityRepository.findAllByEvent(candidateId)) {
            long otherEvent = similarity.getOtherEvent(candidateId);
            if (historyIds.contains(otherEvent)) {
                neighbors.add(new RecommendedEvent(otherEvent, similarity.getScore()));
            }
        }

        neighbors.sort(Comparator.comparingDouble(RecommendedEvent::score).reversed());

        double weightedSum = 0.0;
        double similaritySum = 0.0;
        for (RecommendedEvent neighbor : neighbors.stream().limit(NEIGHBORS_LIMIT).toList()) {
            double similarity = neighbor.score();
            weightedSum += userWeights.getOrDefault(neighbor.eventId(), 0.0) * similarity;
            similaritySum += similarity;
        }

        if (similaritySum == 0.0) {
            return new RecommendedEvent(candidateId, 0.0);
        }

        return new RecommendedEvent(candidateId, weightedSum / similaritySum);
    }
}
