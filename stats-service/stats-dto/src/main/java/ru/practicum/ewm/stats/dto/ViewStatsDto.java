package ru.practicum.ewm.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Агрегированная статистика просмотров URI.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {
    /** Имя приложения, для которого собрана статистика. */
    private String app;

    /** URI, по которому агрегированы посещения. */
    private String uri;

    /** Количество просмотров или уникальных посетителей в зависимости от запроса. */
    private long hits;
}
