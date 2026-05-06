package ewm.event.service;

import ewm.common.exception.NotFoundException;
import ewm.event.client.EventClient;
import ewm.event.model.Event;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.internal.dto.EventInternalDto;
import ru.practicum.ewm.internal.dto.IdsRequest;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventReferenceService {

    private final EventClient eventClient;
    private final EntityManager entityManager;

    public EventInternalDto getExistingEvent(Long eventId) {
        return eventClient.getEvent(eventId);
    }

    public Event getExistingReference(Long eventId) {
        if (!exists(eventId)) {
            throw new NotFoundException("Event not found: " + eventId);
        }
        return entityManager.getReference(Event.class, eventId);
    }

    private boolean exists(Long eventId) {
        Map<Long, Boolean> result = eventClient.existsEvents(new IdsRequest(List.of(eventId)));
        return Boolean.TRUE.equals(result.get(eventId));
    }
}
