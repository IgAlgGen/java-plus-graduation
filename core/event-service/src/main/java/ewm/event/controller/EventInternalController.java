package ewm.event.controller;

import ewm.event.service.EventInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.internal.ApiPaths;
import ru.practicum.ewm.internal.dto.EventInternalDto;
import ru.practicum.ewm.internal.dto.EventShortInternalDto;
import ru.practicum.ewm.internal.dto.IdsRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EventInternalController {

    private final EventInternalService eventInternalService;

    @GetMapping(ApiPaths.EVENTS_BY_ID)
    public EventInternalDto getEvent(@PathVariable("eventId") Long eventId) {
        return eventInternalService.getEvent(eventId);
    }

    @PostMapping(ApiPaths.EVENTS_BATCH)
    public List<EventInternalDto> getEvents(@RequestBody IdsRequest request) {
        return eventInternalService.getEvents(request.ids());
    }

    @PostMapping(ApiPaths.EVENTS_EXISTS_BATCH)
    public Map<Long, Boolean> existsEvents(@RequestBody IdsRequest request) {
        return eventInternalService.existsEvents(request.ids());
    }

    @PostMapping(ApiPaths.EVENTS_SHORT_BATCH)
    public List<EventShortInternalDto> getShortEvents(@RequestBody IdsRequest request) {
        return eventInternalService.getShortEvents(request.ids());
    }

    @GetMapping(ApiPaths.EVENTS_EXISTS_BY_CATEGORY)
    public boolean existsByCategory(@PathVariable("categoryId") Long categoryId) {
        return eventInternalService.existsByCategory(categoryId);
    }
}
