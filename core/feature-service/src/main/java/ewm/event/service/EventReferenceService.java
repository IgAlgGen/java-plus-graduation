package ewm.event.service;

import ewm.common.exception.NotFoundException;
import ewm.event.client.EventClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.internal.dto.EventInternalDto;
import ru.practicum.ewm.internal.dto.IdsRequest;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventReferenceService {

    private final EventClient eventClient;

    public void ensureEventExists(Long eventId) {
        eventClient.getEvent(eventId);
    }

    public void ensurePublished(Long eventId) {
        EventInternalDto event = eventClient.getEvent(eventId);
        if (!"PUBLISHED".equals(event.state())) {
            throw new ewm.common.exception.ConflictException("Comments can only be added to published events");
        }
    }

    public void ensureEventsExist(Set<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return;
        }

        Map<Long, Boolean> existing = eventClient.existsEvents(new IdsRequest(eventIds.stream().toList()));
        boolean allExist = eventIds.stream().allMatch(id -> Boolean.TRUE.equals(existing.get(id)));
        if (!allExist) {
            throw new NotFoundException("Some events not found");
        }
    }
}
