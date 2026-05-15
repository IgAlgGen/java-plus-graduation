package ewm.request.controller;

import ewm.request.dto.EventRequestStatusUpdateRequest;
import ewm.request.dto.EventRequestStatusUpdateResult;
import ewm.request.dto.ParticipationRequestDto;
import ewm.request.service.ParticipationRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер пользовательских операций с заявками на участие.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class ParticipationRequestController {

    private final ParticipationRequestService service;

    /**
     * Создает заявку пользователя на участие в событии.
     *
     * @param userId идентификатор заявителя
     * @param eventId идентификатор события
     * @return созданная заявка
     */
    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable Long userId,
                                          @RequestParam Long eventId) {
        return service.create(userId, eventId);
    }

    /**
     * Отменяет заявку пользователя.
     *
     * @param userId идентификатор заявителя
     * @param requestId идентификатор заявки
     * @return отмененная заявка
     */
    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        return service.cancel(userId, requestId);
    }

    /**
     * Возвращает заявки пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список заявок
     */
    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        return service.getUserRequests(userId);
    }

    /**
     * Возвращает заявки на событие его инициатору.
     *
     * @param userId идентификатор инициатора
     * @param eventId идентификатор события
     * @return список заявок на событие
     */
    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        return service.getEventRequests(userId, eventId);
    }

    /**
     * Подтверждает или отклоняет заявки на событие.
     *
     * @param userId идентификатор инициатора
     * @param eventId идентификатор события
     * @param body запрос со списком заявок и целевым статусом
     * @return результат распределения заявок по подтвержденным и отклоненным
     */
    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequests(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @Valid @RequestBody EventRequestStatusUpdateRequest body) {
        return service.updateEventRequests(userId, eventId, body.getRequestIds(), body.getStatus());
    }
}
