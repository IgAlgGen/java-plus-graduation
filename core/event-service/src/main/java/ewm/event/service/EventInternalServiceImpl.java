package ewm.event.service;

import ewm.common.exception.NotFoundException;
import ewm.event.mapper.EventInternalMapper;
import ewm.event.mapper.EventMapper;
import ewm.event.model.Event;
import ewm.event.repository.DatabaseEventRepository;
import ewm.user.service.UserReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.internal.dto.EventInternalDto;
import ru.practicum.ewm.internal.dto.EventShortInternalDto;
import ru.practicum.ewm.internal.dto.UserInternalDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventInternalServiceImpl implements EventInternalService {

    private final DatabaseEventRepository eventRepository;
    private final UserReferenceService userReferenceService;

    @Override
    @Transactional(readOnly = true)
    public EventInternalDto getEvent(Long eventId) {
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        return EventInternalMapper.toInternalDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventInternalDto> getEvents(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return eventRepository.findAllById(ids).stream()
                .map(EventInternalMapper::toInternalDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Boolean> existsEvents(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        Map<Long, Boolean> result = ids.stream()
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> false,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));

        eventRepository.findAllById(result.keySet())
                .forEach(event -> result.put(event.getId(), true));

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortInternalDto> getShortEvents(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        List<Event> events = eventRepository.findAllById(ids);
        Map<Long, UserInternalDto> initiators = userReferenceService.getUsersByIds(events.stream()
                .map(EventMapper::getInitiatorId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList());

        return events.stream()
                .map(event -> EventInternalMapper.toShortInternalDto(
                        event,
                        initiators.get(EventMapper.getInitiatorId(event))
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCategory(Long categoryId) {
        return eventRepository.existsByCategoryId(categoryId);
    }
}
