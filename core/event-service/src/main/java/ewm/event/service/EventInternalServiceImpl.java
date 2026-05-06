package ewm.event.service;

import ewm.common.exception.NotFoundException;
import ewm.event.mapper.EventInternalMapper;
import ewm.event.model.Event;
import ewm.event.repository.DatabaseEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.internal.dto.EventInternalDto;
import ru.practicum.ewm.internal.dto.EventShortInternalDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventInternalServiceImpl implements EventInternalService {

    private final DatabaseEventRepository eventRepository;

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
        return eventRepository.findAllById(ids).stream()
                .map(EventInternalMapper::toShortInternalDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCategory(Long categoryId) {
        return eventRepository.existsByCategoryId(categoryId);
    }
}
