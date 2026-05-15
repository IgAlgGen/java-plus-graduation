package ru.practicum.ewm.stats.analyzer.service;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;

import java.util.Collection;
import java.util.List;

public interface AnalyzerService {

    void updateUserAction(UserActionAvro userAction);

    void updateEventSimilarity(EventSimilarityAvro eventSimilarity);

    List<RecommendedEvent> getSimilarEvents(long eventId, long userId, int maxResults);

    List<RecommendedEvent> getRecommendationsForUser(long userId, int maxResults);

    List<RecommendedEvent> getInteractionsCount(Collection<Long> eventIds);
}
