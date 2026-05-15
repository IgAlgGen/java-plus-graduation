package ewm.event.controller;

import ewm.event.dto.EventFullDto;
import ewm.event.dto.EventShortDto;
import ewm.event.model.EventSort;
import ewm.event.service.EventService;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicController {
    private final EventService eventService;

    /**
     * Возвращает опубликованное событие и учитывает просмотр пользователя.
     *
     * @param eventId идентификатор события
     * @param request HTTP-запрос для регистрации статистики
     * @return полная информация о событии
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getPublicEvent(@PathVariable("id") Long eventId,
                                       @RequestHeader("X-EWM-USER-ID") long userId) {
        return eventService.getPublicEvent(eventId, userId);
    }

    /**
     * Ищет опубликованные события по публичным фильтрам.
     *
     * @param text текст поиска по аннотации и описанию
     * @param categories идентификаторы категорий
     * @param paid фильтр платности
     * @param rangeStart начало диапазона дат события
     * @param rangeEnd конец диапазона дат события
     * @param onlyAvailable признак поиска событий со свободными местами
     * @param sort сортировка результата
     * @param from смещение первого результата
     * @param size размер страницы
     * @return краткая информация о найденных событиях
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getPublicEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false) EventSort sort,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @PositiveOrZero int size) {
        return eventService.getPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from,
                size);
    }

    @GetMapping("/recommendations")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getRecommendations(@RequestHeader("X-EWM-USER-ID") long userId,
                                                  @RequestParam(defaultValue = "10") @PositiveOrZero int maxResults) {
        return eventService.getRecommendations(userId, maxResults);
    }

    @PutMapping("/{eventId}/like")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto like(@PathVariable Long eventId,
                             @RequestHeader("X-EWM-USER-ID") long userId) {
        return eventService.like(eventId, userId);
    }
}
