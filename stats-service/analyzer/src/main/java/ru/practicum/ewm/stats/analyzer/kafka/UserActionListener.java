package ru.practicum.ewm.stats.analyzer.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.analyzer.service.AnalyzerService;
import ru.practicum.ewm.stats.avro.UserActionAvro;

@Component
public class UserActionListener {

    private final AnalyzerService analyzerService;
    private final AvroDeserializer avroDeserializer;

    public UserActionListener(AnalyzerService analyzerService, AvroDeserializer avroDeserializer) {
        this.analyzerService = analyzerService;
        this.avroDeserializer = avroDeserializer;
    }

    @KafkaListener(
            topics = "${stats.kafka.topic.user-actions}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onUserAction(byte[] payload) {
        UserActionAvro userAction = avroDeserializer.deserialize(payload, UserActionAvro.getClassSchema());
        analyzerService.updateUserAction(userAction);
    }
}
