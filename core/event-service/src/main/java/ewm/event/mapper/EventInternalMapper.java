package ewm.event.mapper;

import ewm.event.model.Event;
import ru.practicum.ewm.internal.dto.EventInternalDto;
import ru.practicum.ewm.internal.dto.EventShortInternalDto;

public class EventInternalMapper {

    public static EventInternalDto toInternalDto(Event event) {
        return new EventInternalDto(
                event.getId(),
                event.getInitiator().getUserId(),
                event.getCategory().getId(),
                event.getTitle(),
                event.getState() == null ? null : event.getState().name(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getEventDate()
        );
    }

    public static EventShortInternalDto toShortInternalDto(Event event) {
        return new EventShortInternalDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getInitiator().getUserId(),
                event.getInitiator().getName(),
                event.getCategory().getId(),
                event.getCategory().getName(),
                event.getEventDate(),
                event.getPaid()
        );
    }
}
