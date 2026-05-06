package ewm.request.service;

import ewm.request.mapper.ParticipationRequestMapper;
import ewm.request.model.RequestStatus;
import ewm.request.repository.ParticipationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.internal.dto.EventConfirmedRequestsInternalDto;
import ru.practicum.ewm.internal.dto.ParticipationRequestInternalDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestInternalServiceImpl implements ParticipationRequestInternalService {

    private final ParticipationRequestRepository requestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventConfirmedRequestsInternalDto> getConfirmedCounts(List<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return List.of();
        }

        return requestRepository.countConfirmedByEventIds(eventIds).stream()
                .map(row -> new EventConfirmedRequestsInternalDto(row.getEventId(), row.getCnt()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestInternalDto> getEventRequests(Long eventId) {
        return requestRepository.findAllByEventId(eventId).stream()
                .map(ParticipationRequestMapper::toInternalDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Boolean> existsUserRequestsForEvents(Long userId, List<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return Map.of();
        }

        Map<Long, Boolean> result = eventIds.stream()
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> false,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));

        requestRepository.findAllByRequesterUserIdAndEventIdIn(userId, result.keySet().stream().toList())
                .forEach(request -> result.put(request.getEvent().getId(), true));

        return result;
    }
}
