package client;

import java.util.Collection;
import java.util.List;

public interface AnalyzerClient {

    List<RecommendedEvent> getRecommendationsForUser(long userId, int maxResults);

    List<RecommendedEvent> getSimilarEvents(long eventId, long userId, int maxResults);

    List<RecommendedEvent> getInteractionsCount(Collection<Long> eventIds);
}
