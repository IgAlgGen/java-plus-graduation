package ewm.event.service;

import ru.practicum.ewm.internal.dto.EventInternalDto;
import ru.practicum.ewm.internal.dto.EventShortInternalDto;

import java.util.List;
import java.util.Map;

public interface EventInternalService {
    EventInternalDto getEvent(Long eventId);

    List<EventInternalDto> getEvents(List<Long> ids);

    Map<Long, Boolean> existsEvents(List<Long> ids);

    List<EventShortInternalDto> getShortEvents(List<Long> ids);

    boolean existsByCategory(Long categoryId);
}
