package ru.practicum.stats.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST-контроллер сервиса статистики.
 *
 * <p>Принимает сведения о посещениях эндпоинтов и возвращает агрегированную статистику просмотров.</p>
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final StatsService statsService;

    /**
     * Сохраняет факт обращения к эндпоинту.
     *
     * @param hit данные приложения, URI, IP-адреса и времени обращения
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@Valid @RequestBody EndpointHitDto hit) {
        statsService.saveHit(hit);
    }

    /**
     * Возвращает статистику посещений за указанный период.
     *
     * @param start начало периода включительно
     * @param end конец периода включительно
     * @param uris необязательный список URI для фильтрации
     * @param unique если {@code true}, учитываются только уникальные IP-адреса
     * @return агрегированная статистика, отсортированная по количеству просмотров по убыванию
     */
    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }
}
