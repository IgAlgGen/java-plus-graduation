package ewm.event.mapper;

import ewm.event.model.Event;
import ru.practicum.ewm.internal.dto.EventInternalDto;
import ru.practicum.ewm.internal.dto.EventShortInternalDto;
import ru.practicum.ewm.internal.dto.UserInternalDto;

public class EventInternalMapper {

    public static EventInternalDto toInternalDto(Event event) {
        return new EventInternalDto(
                event.getId(),
                EventMapper.getInitiatorId(event),
                event.getCategory().getId(),
                event.getTitle(),
                event.getState() == null ? null : event.getState().name(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getEventDate()
        );
    }

    public static EventShortInternalDto toShortInternalDto(Event event, UserInternalDto initiator) {
        return new EventShortInternalDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                EventMapper.getInitiatorId(event),
                initiator == null ? null : initiator.name(),
                event.getCategory().getId(),
                event.getCategory().getName(),
                event.getEventDate(),
                event.getPaid()
        );
    }
}
