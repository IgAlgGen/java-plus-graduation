package ewm.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.ewm.internal.ApiPaths;

@FeignClient(name = "${event.service.id:event-service}")
public interface EventClient {

    @GetMapping(ApiPaths.EVENTS_EXISTS_BY_CATEGORY)
    Boolean existsByCategory(@PathVariable("categoryId") Long categoryId);
}
