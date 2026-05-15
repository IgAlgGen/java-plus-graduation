package ru.practicum.ewm.stats.aggregator;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventSimilarityCalculatorTest {

    private static final Instant NOW = Instant.parse("2026-05-15T10:00:00Z");
    private static final double EPSILON = 0.000001;

    private final EventSimilarityCalculator calculator = new EventSimilarityCalculator();

    @Test
    void shouldCreateSimilarityForFirstCommonUser() {
        assertTrue(calculator.update(1L, 10L, 0.4, NOW).isEmpty());

        List<EventSimilarity> updates = calculator.update(1L, 20L, 0.8, NOW);

        assertEquals(1, updates.size());
        EventSimilarity similarity = updates.getFirst();
        assertEquals(10L, similarity.eventA());
        assertEquals(20L, similarity.eventB());
        assertEquals(0.4, calculator.getMinWeightSum(10L, 20L), EPSILON);
        assertEquals(0.4 / (Math.sqrt(0.4) * Math.sqrt(0.8)), similarity.score(), EPSILON);
    }

    @Test
    void shouldIgnoreActionWhenMaxWeightDoesNotGrow() {
        calculator.update(1L, 10L, 0.8, NOW);
        calculator.update(1L, 20L, 0.8, NOW);
        double score = calculator.getSimilarity(10L, 20L);

        List<EventSimilarity> updates = calculator.update(1L, 10L, 0.4, NOW);

        assertTrue(updates.isEmpty());
        assertEquals(0.8, calculator.getUserEventWeight(10L, 1L), EPSILON);
        assertEquals(score, calculator.getSimilarity(10L, 20L), EPSILON);
    }

    @Test
    void shouldIncrementMinWeightSumOnlyByChangedUserContribution() {
        calculator.update(1L, 10L, 0.4, NOW);
        calculator.update(1L, 20L, 0.8, NOW);

        List<EventSimilarity> updates = calculator.update(1L, 10L, 1.0, NOW);

        assertEquals(1, updates.size());
        assertEquals(0.8, calculator.getMinWeightSum(10L, 20L), EPSILON);
        assertEquals(1.0, calculator.getEventWeightSum(10L), EPSILON);
        assertEquals(0.8, calculator.getEventWeightSum(20L), EPSILON);
        assertEquals(0.8 / (Math.sqrt(1.0) * Math.sqrt(0.8)), updates.getFirst().score(), EPSILON);
    }

    @Test
    void shouldRecalculatePairsWithChangedDenominatorEvenWithoutChangedMinSum() {
        calculator.update(1L, 10L, 0.8, NOW);
        calculator.update(1L, 20L, 0.8, NOW);

        List<EventSimilarity> updates = calculator.update(2L, 10L, 0.4, NOW);

        assertEquals(1, updates.size());
        EventSimilarity pair10And20 = updates.stream()
                .filter(update -> update.eventA() == 10L && update.eventB() == 20L)
                .findFirst()
                .orElseThrow();

        assertEquals(0.8, calculator.getMinWeightSum(10L, 20L), EPSILON);
        assertEquals(0.8 / (Math.sqrt(1.2) * Math.sqrt(0.8)), pair10And20.score(), EPSILON);
    }

    @Test
    void shouldStorePairsInAscendingEventOrder() {
        calculator.update(1L, 20L, 0.8, NOW);

        List<EventSimilarity> updates = calculator.update(1L, 10L, 0.8, NOW);

        assertEquals(1, updates.size());
        assertEquals(10L, updates.getFirst().eventA());
        assertEquals(20L, updates.getFirst().eventB());
        assertEquals(calculator.getMinWeightSum(10L, 20L), calculator.getMinWeightSum(20L, 10L), EPSILON);
        assertEquals(calculator.getSimilarity(10L, 20L), calculator.getSimilarity(20L, 10L), EPSILON);
    }
}
