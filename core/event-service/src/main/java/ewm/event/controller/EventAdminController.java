package ewm.event.controller;

import ewm.event.dto.EventFullDto;
import ewm.event.dto.UpdateEventAdminRequest;
import ewm.event.model.EventState;
import ewm.event.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Административный REST-контроллер событий.
 */
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {
    private final EventService eventService;

    /**
     * Ищет события по административным фильтрам.
     *
     * @param users идентификаторы инициаторов
     * @param states состояния событий
     * @param categories идентификаторы категорий
     * @param rangeStart начало диапазона дат события
     * @param rangeEnd конец диапазона дат события
     * @param from смещение первого результата
     * @param size размер страницы
     * @return найденные события
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> get(@RequestParam(required = false) List<Long> users,
                                  @RequestParam(required = false) List<EventState> states,
                                  @RequestParam(required = false) List<Long> categories,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                  LocalDateTime rangeStart,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                  LocalDateTime rangeEnd,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.get(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /**
     * Обновляет событие от имени администратора.
     *
     * @param eventId идентификатор события
     * @param updateEventAdminRequest изменяемые поля и административное действие
     * @return обновленное событие
     */
    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.update(eventId, updateEventAdminRequest);
    }
}
