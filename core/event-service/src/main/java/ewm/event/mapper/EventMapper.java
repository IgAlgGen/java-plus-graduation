package ewm.event.mapper;

import ewm.category.dto.CategoryDto;
import ewm.common.dto.LocationDto;
import ewm.common.model.Location;
import ewm.event.dto.*;
import ewm.event.model.Event;
import ewm.event.model.EventState;
import ewm.event.model.EventStateAction;
import ewm.user.dto.UserShortDto;
import ru.practicum.ewm.internal.dto.CategoryInternalDto;
import ru.practicum.ewm.internal.dto.UserInternalDto;

public class EventMapper {

    public static Event mapToEvent(Long initiatorId,
                                   NewEventDto eventDto,
                                   Long categoryId) {
        Event event = new Event();
        event.setInitiatorId(initiatorId);
        event.setTitle(eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setCategoryId(categoryId);
        event.setEventDate(eventDto.getEventDate());
        Location location = new Location();
        location.setLat(eventDto.getLocation().getLat());
        location.setLon(eventDto.getLocation().getLon());
        event.setLocation(location);
        event.setPaid(eventDto.getPaid() != null ? eventDto.getPaid() : false);
        event.setParticipantLimit(eventDto.getParticipantLimit() != null ? eventDto.getParticipantLimit() : 0);
        event.setRequestModeration(eventDto.getRequestModeration() != null ? eventDto.getRequestModeration() : true);
        return event;
    }

    public static EventFullDto mapToEventFullDto(Event event,
                                                 long views,
                                                 long confirmedRequests,
                                                 UserInternalDto initiator,
                                                 CategoryInternalDto category) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setCategory(toCategoryDto(category, getCategoryId(event)));
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setInitiator(toUserShortDto(initiator, getInitiatorId(event)));
        LocationDto locationDto = new LocationDto();
        locationDto.setLat(event.getLocation().getLat());
        locationDto.setLon(event.getLocation().getLon());
        eventFullDto.setLocation(locationDto);
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState() == null ? null : event.getState().name());
        eventFullDto.setViews(views);
        eventFullDto.setConfirmedRequests(confirmedRequests);
        return eventFullDto;
    }

    public static EventShortDto mapToEventShortDto(Event event,
                                                   long views,
                                                   long confirmedRequests,
                                                   UserInternalDto initiator,
                                                   CategoryInternalDto category) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(toCategoryDto(category, getCategoryId(event)));
        eventShortDto.setConfirmedRequests(confirmedRequests);
        eventShortDto.setViews(views);
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(toUserShortDto(initiator, getInitiatorId(event)));
        eventShortDto.setPaid(event.getPaid());
        return eventShortDto;
    }

    public static Long getInitiatorId(Event event) {
        return event.getInitiatorId();
    }

    public static Long getCategoryId(Event event) {
        return event.getCategoryId();
    }

    private static UserShortDto toUserShortDto(UserInternalDto user, Long fallbackId) {
        UserShortDto dto = new UserShortDto();
        dto.setId(user == null ? fallbackId : user.id());
        dto.setName(user == null ? null : user.name());
        return dto;
    }

    private static CategoryDto toCategoryDto(CategoryInternalDto category, Long fallbackId) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category == null ? fallbackId : category.id());
        dto.setName(category == null ? null : category.name());
        return dto;
    }

    public static Event updateEvent(Event event, UpdateEventUserRequest updateEventUserRequest) {
        if (updateEventUserRequest.hasTitle()) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        if (updateEventUserRequest.hasAnnotation()) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.hasDescription()) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.hasEventDate()) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.hasLocation()) {
            Location location = new Location();
            location.setLat(updateEventUserRequest.getLocation().getLat());
            location.setLon(updateEventUserRequest.getLocation().getLon());
            event.setLocation(location);
        }
        if (updateEventUserRequest.hasPaid()) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.hasParticipantLimit()) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.hasRequestModeration()) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.hasStateAction()) {
            EventState eventState = updateEventUserRequest.getStateAction() == EventStateAction.SEND_TO_REVIEW
                    ? EventState.PENDING : EventState.CANCELED;
            event.setState(eventState);
        }
        return event;
    }

    public static Event updateEvent(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.hasTitle()) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.hasAnnotation()) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.hasDescription()) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.hasEventDate()) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }

        if (updateEventAdminRequest.hasLocation()) {
            Location location = new Location();
            location.setLat(updateEventAdminRequest.getLocation().getLat());
            location.setLon(updateEventAdminRequest.getLocation().getLon());
            event.setLocation(location);
        }
        if (updateEventAdminRequest.hasPaid()) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.hasParticipantLimit()) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.hasRequestModeration()) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        return event;
    }
}
