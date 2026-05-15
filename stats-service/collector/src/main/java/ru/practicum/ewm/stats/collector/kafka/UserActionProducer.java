package ru.practicum.ewm.stats.collector.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.collector.config.CollectorKafkaProperties;

@Component
@RequiredArgsConstructor
public class UserActionProducer {

    private final KafkaTemplate<Long, byte[]> kafkaTemplate;
    private final CollectorKafkaProperties kafkaProperties;
    private final AvroSerializer avroSerializer;

    public void send(UserActionAvro userAction) {
        byte[] payload = avroSerializer.serialize(userAction);
        kafkaTemplate.send(kafkaProperties.getUserActions(), userAction.getEventId(), payload).join();
    }
}
