package ewm.request.service;

import ru.practicum.ewm.internal.dto.EventConfirmedRequestsInternalDto;
import ru.practicum.ewm.internal.dto.ParticipationRequestInternalDto;

import java.util.List;
import java.util.Map;

public interface ParticipationRequestInternalService {
    List<EventConfirmedRequestsInternalDto> getConfirmedCounts(List<Long> eventIds);

    List<ParticipationRequestInternalDto> getEventRequests(Long eventId);

    Map<Long, Boolean> existsUserRequestsForEvents(Long userId, List<Long> eventIds);
}
