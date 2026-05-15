package ewm.event.controller;

import ewm.event.dto.EventFullDto;
import ewm.event.dto.EventShortDto;
import ewm.event.dto.NewEventDto;
import ewm.event.dto.UpdateEventUserRequest;
import ewm.event.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {
    private final EventService eventService;

    /**
     * Создает событие пользователя.
     *
     * @param userId идентификатор инициатора
     * @param newEventDto данные события
     * @return созданное событие
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId,
                               @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.create(userId, newEventDto);
    }

    /**
     * Возвращает событие, принадлежащее пользователю.
     *
     * @param userId идентификатор инициатора
     * @param eventId идентификатор события
     * @return полная информация о событии
     */
    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto get(@PathVariable Long userId,
                            @PathVariable Long eventId) {
        return eventService.get(userId, eventId);
    }

    /**
     * Возвращает события пользователя.
     *
     * @param userId идентификатор инициатора
     * @param from смещение первого результата
     * @param size размер страницы
     * @return краткая информация о событиях
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.getEvents(userId, from, size);
    }

    /**
     * Обновляет событие пользователем-инициатором.
     *
     * @param userId идентификатор инициатора
     * @param eventId идентификатор события
     * @param updateEventUserRequest изменяемые поля
     * @return обновленное событие
     */
    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return eventService.update(userId, eventId, updateEventUserRequest);
    }
}
