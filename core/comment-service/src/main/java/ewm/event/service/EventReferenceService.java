package ewm.event.service;

import ewm.common.exception.ConflictException;
import ewm.event.client.EventClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.internal.dto.EventInternalDto;

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
            throw new ConflictException("Комментарии можно добавлять только к опубликованным событиям");
        }
    }
}
