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

/**
 * REST-контроллер внутреннего API событий для других сервисов.
 */
@RestController
@RequiredArgsConstructor
public class EventInternalController {

    private final EventInternalService eventInternalService;

    /**
     * Возвращает событие по идентификатору.
     *
     * @param eventId идентификатор события
     * @return внутреннее представление события
     */
    @GetMapping(ApiPaths.EVENTS_BY_ID)
    public EventInternalDto getEvent(@PathVariable("eventId") Long eventId) {
        return eventInternalService.getEvent(eventId);
    }

    /**
     * Возвращает найденные события из переданного набора идентификаторов.
     *
     * @param request запрос со списком идентификаторов
     * @return найденные события
     */
    @PostMapping(ApiPaths.EVENTS_BATCH)
    public List<EventInternalDto> getEvents(@RequestBody IdsRequest request) {
        return eventInternalService.getEvents(request.ids());
    }

    /**
     * Проверяет существование событий.
     *
     * @param request запрос со списком идентификаторов
     * @return карта {@code eventId -> существует}
     */
    @PostMapping(ApiPaths.EVENTS_EXISTS_BATCH)
    public Map<Long, Boolean> existsEvents(@RequestBody IdsRequest request) {
        return eventInternalService.existsEvents(request.ids());
    }

    /**
     * Возвращает краткие представления событий для внешних DTO других сервисов.
     *
     * @param request запрос со списком идентификаторов
     * @return краткие данные событий
     */
    @PostMapping(ApiPaths.EVENTS_SHORT_BATCH)
    public List<EventShortInternalDto> getShortEvents(@RequestBody IdsRequest request) {
        return eventInternalService.getShortEvents(request.ids());
    }

    /**
     * Проверяет, используется ли категория событиями.
     *
     * @param categoryId идентификатор категории
     * @return {@code true}, если в категории есть события
     */
    @GetMapping(ApiPaths.EVENTS_EXISTS_BY_CATEGORY)
    public boolean existsByCategory(@PathVariable("categoryId") Long categoryId) {
        return eventInternalService.existsByCategory(categoryId);
    }
}
