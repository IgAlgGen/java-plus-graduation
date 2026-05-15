package ewm.event.service;

import client.ActionType;
import client.AnalyzerClient;
import client.CollectorClient;
import client.RecommendedEvent;
import ewm.event.model.Event;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatsResilienceService {

    private static final long ANONYMOUS_USER_ID = 0L;

    private final CollectorClient collectorClient;
    private final AnalyzerClient analyzerClient;

    @Retry(name = "statsHit")
    @CircuitBreaker(name = "statsHit", fallbackMethod = "ignoreHit")
    public void collectView(String requestUri) {
        extractEventId(requestUri)
                .ifPresent(eventId -> collectorClient.collectUserAction(ANONYMOUS_USER_ID, eventId, ActionType.VIEW));
    }

    @Retry(name = "statsViews")
    @CircuitBreaker(name = "statsViews", fallbackMethod = "emptyViews")
    public Map<Long, Integer> getEventsViews(List<Event> eventList) {
        if (eventList == null || eventList.isEmpty()) {
            return Map.of();
        }

        List<Long> eventIds = eventList.stream()
                .map(Event::getId)
                .toList();

        List<RecommendedEvent> stats = analyzerClient.getInteractionsCount(eventIds);

        Map<Long, Integer> views = new HashMap<>();
        for (RecommendedEvent stat : stats) {
            views.put(stat.eventId(), (int) Math.ceil(stat.score()));
        }
        return views;
    }

    @SuppressWarnings("unused")
    private void ignoreHit(String requestUri, Throwable throwable) {
    }

    @SuppressWarnings("unused")
    private Map<Long, Integer> emptyViews(List<Event> eventList, Throwable throwable) {
        return Map.of();
    }

    private Optional<Long> extractEventId(String requestUri) {
        if (requestUri == null) {
            return Optional.empty();
        }

        String[] parts = requestUri.split("/");
        if (parts.length != 3 || !"events".equals(parts[1])) {
            return Optional.empty();
        }

        try {
            return Optional.of(Long.parseLong(parts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }
}
