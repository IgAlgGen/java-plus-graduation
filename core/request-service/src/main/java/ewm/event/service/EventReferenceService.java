package ewm.event.service;

import ewm.event.client.EventClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.internal.dto.EventInternalDto;

@Service
@RequiredArgsConstructor
public class EventReferenceService {

    private final EventClient eventClient;

    public EventInternalDto getExistingEvent(Long eventId) {
        return eventClient.getEvent(eventId);
    }
}
