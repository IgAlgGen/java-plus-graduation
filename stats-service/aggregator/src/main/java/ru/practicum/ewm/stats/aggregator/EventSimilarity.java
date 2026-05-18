package ru.practicum.ewm.stats.aggregator;

import java.time.Instant;

public record EventSimilarity(long eventA, long eventB, double score, Instant timestamp) {

    public EventSimilarity(EventPair pair, double score, Instant timestamp) {
        this(pair.eventA(), pair.eventB(), score, timestamp);
    }
}
