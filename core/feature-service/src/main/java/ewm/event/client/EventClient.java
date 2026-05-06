package ewm.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.internal.ApiPaths;
import ru.practicum.ewm.internal.dto.EventShortInternalDto;
import ru.practicum.ewm.internal.dto.IdsRequest;

import java.util.List;
import java.util.Map;

@FeignClient(name = "${event.service.id:event-service}")
public interface EventClient {

    @GetMapping(ApiPaths.EVENTS_EXISTS_BY_CATEGORY)
    Boolean existsByCategory(@PathVariable("categoryId") Long categoryId);

    @PostMapping(ApiPaths.EVENTS_EXISTS_BATCH)
    Map<Long, Boolean> existsEvents(@RequestBody IdsRequest request);

    @PostMapping(ApiPaths.EVENTS_SHORT_BATCH)
    List<EventShortInternalDto> getShortEvents(@RequestBody IdsRequest request);
}
