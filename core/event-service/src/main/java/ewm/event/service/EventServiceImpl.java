package ewm.event.service;

import ewm.category.service.CategoryReferenceService;
import ewm.common.exception.BadRequestException;
import ewm.common.exception.ConflictException;
import ewm.common.exception.NotFoundException;
import ewm.event.dto.*;
import ewm.event.mapper.EventMapper;
import ewm.event.model.Event;
import ewm.event.model.EventSort;
import ewm.event.model.EventState;
import ewm.event.model.EventStateActionAdmin;
import ewm.event.repository.DatabaseEventSearchRepository;
import ewm.event.repository.EventRepository;
import ewm.request.client.RequestClient;
import ewm.user.service.UserReferenceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.internal.dto.EventConfirmedRequestsInternalDto;
import ru.practicum.ewm.internal.dto.IdsRequest;
import ru.practicum.ewm.internal.dto.CategoryInternalDto;
import ru.practicum.ewm.internal.dto.UserInternalDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация бизнес-правил работы с событиями.
 *
 * <p>Создание и пользовательское обновление требуют дату события минимум через два часа.
 * Публичные методы возвращают только опубликованные события и отправляют обращения в сервис статистики.</p>
 */
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserReferenceService userReferenceService;
    private final EventRepository eventRepository;
    private final DatabaseEventSearchRepository  databaseEventSearchRepository;
    private final CategoryReferenceService categoryReferenceService;
    private final StatsResilienceService statsResilienceService;
    private final RequestClient requestClient;

    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        isEventTimeValid(eventDto.getEventDate());

        userReferenceService.ensureExists(userId);
        categoryReferenceService.ensureExists(eventDto.getCategory());

        Event event = EventMapper.mapToEvent(userId, eventDto, eventDto.getCategory());
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event = eventRepository.save(event);

        List<Event> eventList = List.of(event);

        return this.mapToEventFullDto(eventList).getFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto get(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (!EventMapper.getInitiatorId(event).equals(userId)) {
            throw new NotFoundException("Событие не найдено");
        }

        List<Event> eventList = List.of(event);

        return this.mapToEventFullDto(eventList).getFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> get(List<Long> users,
                                  List<EventState> states,
                                  List<Long> categories,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  int from,
                                  int size) {
        Pageable page = PageRequest.of(from / size, size);

        List<Event> eventList = databaseEventSearchRepository.findForAdmin(users, states, categories, rangeStart, rangeEnd, page);

        return this.mapToEventFullDto(eventList);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getPublicEvent(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие не опубликовано");
        }

        List<Event> eventList = List.of(event);
        registerHit(request);

        return this.mapToEventFullDto(eventList).getFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEvents(Long userId, int from, int size) {
        userReferenceService.ensureExists(userId);

        Pageable page = PageRequest.of(from / size, size);

        List<Event> eventList = eventRepository.findByInitiatorId(userId, page);

        return this.mapToEventShortDto(eventList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getPublicEvents(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Boolean onlyAvailable,
                                               EventSort sort,
                                               int from,
                                               int size,
                                               HttpServletRequest request) {

        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("Дата начала диапазона должна быть раньше даты окончания");
        }

        // Pageable: сортировка только по eventDate, не по views
        Pageable page;
        if (sort == EventSort.EVENT_DATE) {
            page = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "eventDate"));
        } else {
            page = PageRequest.of(from / size, size);
        }

        registerHit(request);

        List<Event> eventList = databaseEventSearchRepository.findPublicEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, page
        );

        List<EventShortDto> dtos = mapToEventShortDto(eventList);

        if (sort == EventSort.VIEWS) {
            dtos.sort(Comparator.comparingLong(EventShortDto::getViews).reversed());
        }

        return dtos;
    }


    @Override
    @Transactional
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event currentEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (currentEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Событие уже опубликовано");
        }

        if (!EventMapper.getInitiatorId(currentEvent).equals(userId)) {
            throw new BadRequestException("Пользователь не может редактировать это событие");
        }

        Event updatedEvent = EventMapper.updateEvent(currentEvent, updateEventUserRequest);

        if (updateEventUserRequest.hasCategory() &&
                !EventMapper.getCategoryId(updatedEvent).equals(updateEventUserRequest.getCategory())) {
            categoryReferenceService.ensureExists(updateEventUserRequest.getCategory());
            updatedEvent.setCategoryId(updateEventUserRequest.getCategory());
        }

        isEventTimeValid(updatedEvent.getEventDate());
        updatedEvent = eventRepository.save(updatedEvent);


        List<Event> eventList = List.of(updatedEvent);

        return this.mapToEventFullDto(eventList).getFirst();
    }

    @Override
    @Transactional
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event currentEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (currentEvent.getState().equals(EventState.PUBLISHED) &&
                updateEventAdminRequest.getEventDate() != null &&
                updateEventAdminRequest.getEventDate().isAfter(currentEvent.getPublishedOn().minusHours(1))) {
            throw new ConflictException("Некорректное время события");
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            if (currentEvent.getState().equals(EventState.PENDING)) {
                if (updateEventAdminRequest.getStateAction().equals(EventStateActionAdmin.PUBLISH_EVENT)) {
                    currentEvent.setState(EventState.PUBLISHED);
                    currentEvent.setPublishedOn(LocalDateTime.now());
                } else {
                    currentEvent.setState(EventState.CANCELED);
                }
            } else {
                throw new ConflictException("Некорректный статус события");
            }
        }

        Event updatedEvent = EventMapper.updateEvent(currentEvent, updateEventAdminRequest);

        if (updateEventAdminRequest.hasCategory() &&
                !EventMapper.getCategoryId(updatedEvent).equals(updateEventAdminRequest.getCategory())) {
            categoryReferenceService.ensureExists(updateEventAdminRequest.getCategory());
            updatedEvent.setCategoryId(updateEventAdminRequest.getCategory());
        }

        updatedEvent = eventRepository.save(updatedEvent);

        List<Event> eventList = List.of(updatedEvent);

        return this.mapToEventFullDto(eventList).getFirst();
    }

    private void isEventTimeValid(LocalDateTime eventTime) {
        if (eventTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Некорректное время события");
        }
    }

    private void registerHit(HttpServletRequest request) {
        statsResilienceService.collectView(request.getRequestURI());
    }

    private List<EventFullDto> mapToEventFullDto(List<Event> eventList) {
        Map<Long, Integer> views = statsResilienceService.getEventsViews(eventList);
        Map<Long, Long> confirmed = getConfirmedRequests(eventList);
        Map<Long, UserInternalDto> initiators = getInitiators(eventList);
        Map<Long, CategoryInternalDto> categories = getCategories(eventList);

        return eventList.stream()
                .map(e -> EventMapper.mapToEventFullDto(
                        e,
                        views.getOrDefault(e.getId(), 0),
                        confirmed.getOrDefault(e.getId(), 0L),
                        initiators.get(EventMapper.getInitiatorId(e)),
                        categories.get(EventMapper.getCategoryId(e))
                ))
                .toList();
    }


    private Map<Long, Long> getConfirmedRequests(List<Event> eventList) {
        if (eventList == null || eventList.isEmpty()) return Map.of();

        List<Long> ids = eventList.stream()
                .map(Event::getId)
                .toList();

        Map<Long, Long> map = new HashMap<>();
        for (EventConfirmedRequestsInternalDto row : requestClient.getConfirmedCounts(new IdsRequest(ids))) {
            map.put(row.eventId(), row.confirmedRequests());
        }
        return map;
    }


    private List<EventShortDto> mapToEventShortDto(List<Event> eventList) {
        Map<Long, Integer> views = statsResilienceService.getEventsViews(eventList);
        Map<Long, Long> confirmed = getConfirmedRequests(eventList);
        Map<Long, UserInternalDto> initiators = getInitiators(eventList);
        Map<Long, CategoryInternalDto> categories = getCategories(eventList);

        return eventList.stream()
                .map(e -> EventMapper.mapToEventShortDto(
                        e,
                        views.getOrDefault(e.getId(), 0),
                        confirmed.getOrDefault(e.getId(), 0L),
                        initiators.get(EventMapper.getInitiatorId(e)),
                        categories.get(EventMapper.getCategoryId(e))
                ))
                .toList();
    }

    private Map<Long, UserInternalDto> getInitiators(List<Event> eventList) {
        if (eventList == null || eventList.isEmpty()) return Map.of();

        List<Long> ids = eventList.stream()
                .map(EventMapper::getInitiatorId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();

        return userReferenceService.getUsersByIds(ids);
    }

    private Map<Long, CategoryInternalDto> getCategories(List<Event> eventList) {
        if (eventList == null || eventList.isEmpty()) return Map.of();

        List<Long> ids = eventList.stream()
                .map(EventMapper::getCategoryId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();

        return categoryReferenceService.getCategoriesByIds(ids);
    }
}
