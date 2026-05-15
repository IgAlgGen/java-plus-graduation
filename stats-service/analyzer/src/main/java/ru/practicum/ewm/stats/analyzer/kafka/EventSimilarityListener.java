package ru.practicum.ewm.stats.analyzer.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.analyzer.service.AnalyzerService;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

@Component
public class EventSimilarityListener {

    private final AnalyzerService analyzerService;
    private final AvroDeserializer avroDeserializer;

    public EventSimilarityListener(AnalyzerService analyzerService, AvroDeserializer avroDeserializer) {
        this.analyzerService = analyzerService;
        this.avroDeserializer = avroDeserializer;
    }

    @KafkaListener(
            topics = "${stats.kafka.topic.events-similarity}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onEventSimilarity(byte[] payload) {
        EventSimilarityAvro eventSimilarity =
                avroDeserializer.deserialize(payload, EventSimilarityAvro.getClassSchema());
        analyzerService.updateEventSimilarity(eventSimilarity);
    }
}
