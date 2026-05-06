package ewm.event.service;

import ewm.common.exception.NotFoundException;
import ewm.event.client.EventClient;
import ewm.event.model.Event;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.internal.dto.IdsRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventReferenceService {

    private final EventClient eventClient;
    private final EntityManager entityManager;

    public Set<Event> getExistingReferences(Set<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return Set.of();
        }

        Map<Long, Boolean> existing = eventClient.existsEvents(new IdsRequest(eventIds.stream().toList()));
        boolean allExist = eventIds.stream().allMatch(id -> Boolean.TRUE.equals(existing.get(id)));
        if (!allExist) {
            throw new NotFoundException("Some events not found");
        }

        return eventIds.stream()
                .map(id -> entityManager.getReference(Event.class, id))
                .collect(java.util.stream.Collectors.toSet());
    }
}
