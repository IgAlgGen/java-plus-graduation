package ru.practicum.ewm.stats.aggregator.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.aggregator.config.AggregatorKafkaProperties;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

@Component
public class EventSimilarityProducer {

    private final KafkaTemplate<Long, byte[]> kafkaTemplate;
    private final AggregatorKafkaProperties kafkaProperties;
    private final AvroCodec avroCodec;

    public EventSimilarityProducer(KafkaTemplate<Long, byte[]> kafkaTemplate,
                                   AggregatorKafkaProperties kafkaProperties,
                                   AvroCodec avroCodec) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProperties = kafkaProperties;
        this.avroCodec = avroCodec;
    }

    public void send(EventSimilarityAvro eventSimilarity) {
        byte[] payload = avroCodec.serialize(eventSimilarity);
        kafkaTemplate.send(kafkaProperties.getEventsSimilarity(), 0, eventSimilarity.getEventA(), payload).join();
    }
}
