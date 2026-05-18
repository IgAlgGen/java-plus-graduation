package ru.practicum.ewm.stats.aggregator.mapper;

import ru.practicum.ewm.stats.aggregator.EventSimilarity;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

public final class EventSimilarityMapper {

    private EventSimilarityMapper() {
    }

    public static EventSimilarityAvro toAvro(EventSimilarity similarity) {
        return EventSimilarityAvro.newBuilder()
                .setEventA(similarity.eventA())
                .setEventB(similarity.eventB())
                .setScore(similarity.score())
                .setTimestamp(similarity.timestamp())
                .build();
    }
}
