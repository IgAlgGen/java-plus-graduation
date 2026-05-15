package ewm.event.service;

import client.StatsClient;
import ewm.event.model.Event;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsResilienceService {

    private final StatsClient statsClient;

    @Retry(name = "statsHit")
    @CircuitBreaker(name = "statsHit", fallbackMethod = "ignoreHit")
    public void hit(EndpointHitDto endpointHitDto) {
        statsClient.hit(endpointHitDto);
    }

    @Retry(name = "statsViews")
    @CircuitBreaker(name = "statsViews", fallbackMethod = "emptyViews")
    public Map<Long, Integer> getEventsViews(List<Event> eventList) {
        if (eventList == null || eventList.isEmpty()) {
            return Map.of();
        }

        List<String> uris = eventList.stream()
                .map(event -> "/events/" + event.getId())
                .toList();

        LocalDateTime start = eventList.stream()
                .map(Event::getCreatedOn)
                .filter(java.util.Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(LocalDateTime.now().minusYears(1));

        LocalDateTime end = LocalDateTime.now();

        List<ViewStatsDto> stats = statsClient.getStats(start, end, uris, true);

        Map<Long, Integer> views = new HashMap<>();
        for (ViewStatsDto stat : stats) {
            String[] parts = stat.getUri().split("/");
            if (parts.length >= 3) {
                long eventId = Long.parseLong(parts[2]);
                views.put(eventId, (int) stat.getHits());
            }
        }
        return views;
    }

    @SuppressWarnings("unused")
    private void ignoreHit(EndpointHitDto endpointHitDto, Throwable throwable) {
    }

    @SuppressWarnings("unused")
    private Map<Long, Integer> emptyViews(List<Event> eventList, Throwable throwable) {
        return Map.of();
    }
}
