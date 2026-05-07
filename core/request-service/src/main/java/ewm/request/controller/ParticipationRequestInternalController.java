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

/**
 * REST-контроллер внутреннего API заявок на участие.
 */
@RestController
@RequiredArgsConstructor
public class ParticipationRequestInternalController {

    private final ParticipationRequestInternalService service;

    /**
     * Возвращает количество подтвержденных заявок по событиям.
     *
     * @param request запрос со списком идентификаторов событий
     * @return счетчики подтвержденных заявок
     */
    @PostMapping(ApiPaths.REQUESTS_CONFIRMED_COUNTS)
    public List<EventConfirmedRequestsInternalDto> getConfirmedCounts(@RequestBody IdsRequest request) {
        return service.getConfirmedCounts(request.ids());
    }

    /**
     * Возвращает все заявки события.
     *
     * @param eventId идентификатор события
     * @return заявки события
     */
    @GetMapping(ApiPaths.REQUESTS_BY_EVENT)
    public List<ParticipationRequestInternalDto> getEventRequests(@PathVariable("eventId") Long eventId) {
        return service.getEventRequests(eventId);
    }

    /**
     * Проверяет, подавал ли пользователь заявки на указанные события.
     *
     * @param userId идентификатор пользователя
     * @param request запрос со списком идентификаторов событий
     * @return карта {@code eventId -> заявка существует}
     */
    @PostMapping(ApiPaths.REQUESTS_USER_EVENTS_EXISTS)
    public Map<Long, Boolean> existsUserRequestsForEvents(@PathVariable("userId") Long userId,
                                                          @RequestBody IdsRequest request) {
        return service.existsUserRequestsForEvents(userId, request.ids());
    }
}
