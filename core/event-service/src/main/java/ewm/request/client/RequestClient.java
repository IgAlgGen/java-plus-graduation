package ewm.request.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.ewm.internal.ApiPaths;
import ru.practicum.ewm.internal.dto.EventConfirmedRequestsInternalDto;
import ru.practicum.ewm.internal.dto.IdsRequest;

import java.util.List;
import java.util.Map;

@FeignClient(name = "${request.service.id:request-service}")
public interface RequestClient {

    @PostMapping(ApiPaths.REQUESTS_CONFIRMED_COUNTS)
    List<EventConfirmedRequestsInternalDto> getConfirmedCounts(@RequestBody IdsRequest request);

    @PostMapping(ApiPaths.REQUESTS_USER_CONFIRMED_EVENTS_EXISTS)
    Map<Long, Boolean> existsUserConfirmedRequestsForEvents(@PathVariable("userId") Long userId,
                                                            @RequestBody IdsRequest request);
}
