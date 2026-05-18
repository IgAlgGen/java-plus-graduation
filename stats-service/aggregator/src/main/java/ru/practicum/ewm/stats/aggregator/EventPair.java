package ru.practicum.ewm.stats.aggregator;

public record EventPair(long eventA, long eventB) {

    public EventPair {
        if (eventA == eventB) {
            throw new IllegalArgumentException("Pair must contain different events");
        }
        if (eventA > eventB) {
            long first = eventB;
            eventB = eventA;
            eventA = first;
        }
    }
}
