package ewm.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.ewm.internal.ApiPaths;
import ru.practicum.ewm.internal.dto.EventInternalDto;

@FeignClient(name = "${event.service.id:event-service}")
public interface EventClient {

    @GetMapping(ApiPaths.EVENTS_BY_ID)
    EventInternalDto getEvent(@PathVariable("eventId") Long eventId);
}
