package ru.practicum.ewm.stats.aggregator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EventSimilarityCalculator {

    private final Map<Long, Map<Long, Double>> eventUserWeights = new HashMap<>();
    private final Map<Long, Double> eventWeightSums = new HashMap<>();
    private final Map<EventPair, Double> minWeightSums = new HashMap<>();

    public List<EventSimilarity> update(long userId, long eventId, double weight, Instant timestamp) {
        if (weight <= 0.0) {
            throw new IllegalArgumentException("Вес должен быть положительным");
        }
        Objects.requireNonNull(timestamp, "Временная метка не должна быть нулевой");

        Map<Long, Double> userWeights = eventUserWeights.computeIfAbsent(eventId, ignored -> new HashMap<>());
        double oldWeight = userWeights.getOrDefault(userId, 0.0);
        if (weight <= oldWeight) {
            return List.of();
        }

        double deltaWeight = weight - oldWeight;
        userWeights.put(userId, weight);
        eventWeightSums.merge(eventId, deltaWeight, Double::sum);

        List<EventSimilarity> recalculated = new ArrayList<>();
        for (long otherEventId : eventUserWeights.keySet()) {
            if (otherEventId == eventId) {
                continue;
            }

            EventPair pair = new EventPair(eventId, otherEventId);
            Double otherWeight = eventUserWeights.get(otherEventId).get(userId);
            if (otherWeight == null) {
                continue;
            }

            double deltaMin = Math.min(weight, otherWeight) - Math.min(oldWeight, otherWeight);
            if (deltaMin > 0.0) {
                minWeightSums.merge(pair, deltaMin, Double::sum);
            }

            double minWeightSum = minWeightSums.getOrDefault(pair, 0.0);
            if (minWeightSum > 0.0) {
                recalculated.add(new EventSimilarity(pair, calculateScore(pair), timestamp));
            }
        }

        recalculated.sort(Comparator
                .comparingLong(EventSimilarity::eventA)
                .thenComparingLong(EventSimilarity::eventB));
        return recalculated;
    }

    public double getUserEventWeight(long eventId, long userId) {
        return eventUserWeights.getOrDefault(eventId, Map.of()).getOrDefault(userId, 0.0);
    }

    public double getEventWeightSum(long eventId) {
        return eventWeightSums.getOrDefault(eventId, 0.0);
    }

    public double getMinWeightSum(long eventA, long eventB) {
        return minWeightSums.getOrDefault(new EventPair(eventA, eventB), 0.0);
    }

    public double getSimilarity(long eventA, long eventB) {
        return calculateScore(new EventPair(eventA, eventB));
    }

    private double calculateScore(EventPair pair) {
        double minWeightSum = minWeightSums.getOrDefault(pair, 0.0);
        double eventASum = eventWeightSums.getOrDefault(pair.eventA(), 0.0);
        double eventBSum = eventWeightSums.getOrDefault(pair.eventB(), 0.0);
        if (minWeightSum == 0.0 || eventASum == 0.0 || eventBSum == 0.0) {
            return 0.0;
        }
        return minWeightSum / (Math.sqrt(eventASum) * Math.sqrt(eventBSum));
    }
}
