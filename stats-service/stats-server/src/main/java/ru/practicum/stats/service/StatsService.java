package ru.practicum.stats.service;

import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис сохранения посещений и расчета статистики просмотров.
 */
public interface StatsService {
    /**
     * Сохраняет одно обращение к эндпоинту.
     *
     * @param hit данные посещения
     */
    void saveHit(EndpointHitDto hit);

    /**
     * Возвращает статистику посещений за период.
     *
     * @param start начало периода включительно
     * @param end конец периода включительно; должно быть не раньше {@code start}
     * @param uris URI, по которым нужно ограничить выборку; пустой список означает все URI
     * @param unique если {@code true}, один IP-адрес учитывается один раз для каждого URI
     * @return агрегированные просмотры по приложениям и URI
     * @throws ru.practicum.stats.exception.BadRequestException если конец периода раньше начала
     */
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
