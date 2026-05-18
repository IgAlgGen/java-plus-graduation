package ru.practicum.ewm.stats.aggregator.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.aggregator.EventSimilarity;
import ru.practicum.ewm.stats.aggregator.EventSimilarityCalculator;
import ru.practicum.ewm.stats.aggregator.mapper.EventSimilarityMapper;
import ru.practicum.ewm.stats.avro.ActionWeight;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;

import java.util.List;

@Component
public class UserActionListener {

    private final EventSimilarityCalculator similarityCalculator;
    private final EventSimilarityProducer eventSimilarityProducer;
    private final AvroCodec avroCodec;

    public UserActionListener(EventSimilarityCalculator similarityCalculator,
                              EventSimilarityProducer eventSimilarityProducer,
                              AvroCodec avroCodec) {
        this.similarityCalculator = similarityCalculator;
        this.eventSimilarityProducer = eventSimilarityProducer;
        this.avroCodec = avroCodec;
    }

    @KafkaListener(
            topics = "${stats.kafka.topic.user-actions}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onUserAction(byte[] payload) {
        UserActionAvro userAction = avroCodec.deserialize(payload, UserActionAvro.getClassSchema());
        double weight = ActionWeight.getWeight(userAction.getActionType());

        List<EventSimilarity> similarities;
        synchronized (similarityCalculator) {
            similarities = similarityCalculator.update(
                    userAction.getUserId(),
                    userAction.getEventId(),
                    weight,
                    userAction.getTimestamp()
            );
        }

        for (EventSimilarity similarity : similarities) {
            EventSimilarityAvro eventSimilarity = EventSimilarityMapper.toAvro(similarity);
            eventSimilarityProducer.send(eventSimilarity);
        }
    }
}
