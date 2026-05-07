package ewm.event.mapper;

import ewm.event.model.Event;
import ru.practicum.ewm.internal.dto.EventInternalDto;
import ru.practicum.ewm.internal.dto.EventShortInternalDto;
import ru.practicum.ewm.internal.dto.CategoryInternalDto;
import ru.practicum.ewm.internal.dto.UserInternalDto;

/**
 * Маппер внутренних представлений события для межсервисного API.
 */
public class EventInternalMapper {

    public static EventInternalDto toInternalDto(Event event) {
        return new EventInternalDto(
                event.getId(),
                EventMapper.getInitiatorId(event),
                EventMapper.getCategoryId(event),
                event.getTitle(),
                event.getState() == null ? null : event.getState().name(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getEventDate()
        );
    }

    public static EventShortInternalDto toShortInternalDto(Event event,
                                                          UserInternalDto initiator,
                                                          CategoryInternalDto category) {
        return new EventShortInternalDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                EventMapper.getInitiatorId(event),
                initiator == null ? null : initiator.name(),
                EventMapper.getCategoryId(event),
                category == null ? null : category.name(),
                event.getEventDate(),
                event.getPaid()
        );
    }
}
