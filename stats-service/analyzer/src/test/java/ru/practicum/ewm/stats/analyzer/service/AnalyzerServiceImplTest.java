package ru.practicum.ewm.stats.analyzer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.ewm.stats.analyzer.model.EventSimilarityEntity;
import ru.practicum.ewm.stats.analyzer.model.EventSimilarityId;
import ru.practicum.ewm.stats.analyzer.model.UserEventInteractionEntity;
import ru.practicum.ewm.stats.analyzer.model.UserEventInteractionId;
import ru.practicum.ewm.stats.analyzer.repository.EventSimilarityRepository;
import ru.practicum.ewm.stats.analyzer.repository.UserEventInteractionRepository;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

@DataJpaTest(properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.sql.init.mode=always"
})
@Import(AnalyzerServiceImpl.class)
class AnalyzerServiceImplTest {

    private static final Instant BASE_TIME = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private AnalyzerService analyzerService;

    @Autowired
    private UserEventInteractionRepository interactionRepository;

    @Autowired
    private EventSimilarityRepository similarityRepository;

    @Test
    void getSimilarEventsShouldExcludeInteractedEventsAndSortByScore() {
        interaction(1, 20, 1.0, BASE_TIME);
        similarity(10, 20, 0.95);
        similarity(10, 30, 0.70);
        similarity(10, 40, 0.90);
        similarity(50, 60, 0.99);

        List<RecommendedEvent> recommendations = analyzerService.getSimilarEvents(10, 1, 2);

        assertThat(recommendations)
                .extracting(RecommendedEvent::eventId)
                .containsExactly(40L, 30L);
        assertThat(recommendations)
                .extracting(RecommendedEvent::score)
                .containsExactly(0.90, 0.70);
    }

    @Test
    void getRecommendationsForUserShouldPredictScoresFromSimilarHistoryEvents() {
        interaction(1, 10, 1.0, BASE_TIME.plusSeconds(10));
        interaction(1, 20, 0.8, BASE_TIME.plusSeconds(20));
        interaction(2, 40, 1.0, BASE_TIME.plusSeconds(30));

        similarity(10, 30, 0.50);
        similarity(20, 30, 0.90);
        similarity(10, 40, 0.80);
        similarity(20, 40, 0.20);
        similarity(10, 20, 0.99);

        List<RecommendedEvent> recommendations = analyzerService.getRecommendationsForUser(1, 2);

        assertThat(recommendations)
                .extracting(RecommendedEvent::eventId)
                .containsExactly(40L, 30L);
        assertThat(recommendations.get(0).score()).isCloseTo(0.96, within(0.000001));
        assertThat(recommendations.get(1).score()).isCloseTo(0.8714285714, within(0.000001));
    }

    @Test
    void getRecommendationsForUserShouldReturnEmptyWhenUserHasNoHistory() {
        similarity(10, 20, 0.9);

        assertThat(analyzerService.getRecommendationsForUser(999, 10)).isEmpty();
    }

    @Test
    void getInteractionsCountShouldReturnWeightSumsAndZeroForMissingEvents() {
        interaction(1, 10, 0.4, BASE_TIME);
        interaction(2, 10, 1.0, BASE_TIME);
        interaction(1, 20, 0.8, BASE_TIME);

        List<RecommendedEvent> counts = analyzerService.getInteractionsCount(List.of(10L, 20L, 30L, 10L));

        assertThat(counts)
                .extracting(RecommendedEvent::eventId)
                .containsExactly(10L, 20L, 30L);
        assertThat(counts.get(0).score()).isCloseTo(1.4, within(0.000001));
        assertThat(counts.get(1).score()).isCloseTo(0.8, within(0.000001));
        assertThat(counts.get(2).score()).isZero();
    }

    private void interaction(long userId, long eventId, double weight, Instant updatedAt) {
        interactionRepository.save(new UserEventInteractionEntity(
                new UserEventInteractionId(userId, eventId),
                weight,
                updatedAt
        ));
    }

    private void similarity(long eventA, long eventB, double score) {
        similarityRepository.save(new EventSimilarityEntity(
                new EventSimilarityId(eventA, eventB),
                score,
                BASE_TIME
        ));
    }
}
