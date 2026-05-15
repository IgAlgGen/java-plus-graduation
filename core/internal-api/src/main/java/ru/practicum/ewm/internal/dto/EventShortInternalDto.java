package ru.practicum.ewm.internal.dto;

import java.time.LocalDateTime;

/**
 * Краткое внутреннее представление события для сборки внешних DTO во внешних сервисах.
 *
 * @param id идентификатор события
 * @param title заголовок события
 * @param annotation краткая аннотация события
 * @param initiatorId идентификатор инициатора
 * @param initiatorName имя инициатора
 * @param categoryId идентификатор категории
 * @param categoryName название категории
 * @param eventDate дата и время проведения события
 * @param paid признак платного участия
 */
public record EventShortInternalDto(
        Long id,
        String title,
        String annotation,
        Long initiatorId,
        String initiatorName,
        Long categoryId,
        String categoryName,
        LocalDateTime eventDate,
        Boolean paid
) {
}
