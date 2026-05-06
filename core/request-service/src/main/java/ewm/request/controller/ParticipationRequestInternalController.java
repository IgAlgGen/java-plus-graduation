package ewm.request.controller;

import ewm.request.service.ParticipationRequestInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.internal.ApiPaths;
import ru.practicum.ewm.internal.dto.EventConfirmedRequestsInternalDto;
import ru.practicum.ewm.internal.dto.IdsRequest;
import ru.practicum.ewm.internal.dto.ParticipationRequestInternalDto;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ParticipationRequestInternalController {

    private final ParticipationRequestInternalService service;

    @PostMapping(ApiPaths.REQUESTS_CONFIRMED_COUNTS)
    public List<EventConfirmedRequestsInternalDto> getConfirmedCounts(@RequestBody IdsRequest request) {
        return service.getConfirmedCounts(request.ids());
    }

    @GetMapping(ApiPaths.REQUESTS_BY_EVENT)
    public List<ParticipationRequestInternalDto> getEventRequests(@PathVariable("eventId") Long eventId) {
        return service.getEventRequests(eventId);
    }

    @PostMapping(ApiPaths.REQUESTS_USER_EVENTS_EXISTS)
    public Map<Long, Boolean> existsUserRequestsForEvents(@PathVariable("userId") Long userId,
                                                          @RequestBody IdsRequest request) {
        return service.existsUserRequestsForEvents(userId, request.ids());
    }
}
