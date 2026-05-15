package ru.practicum.ewm.internal.dto;

import java.time.LocalDateTime;

/**
 * Внутреннее представление события для проверки правил в соседних сервисах.
 *
 * @param id идентификатор события
 * @param initiatorId идентификатор инициатора
 * @param categoryId идентификатор категории
 * @param title заголовок события
 * @param state состояние события
 * @param participantLimit лимит участников; {@code 0} означает отсутствие лимита
 * @param requestModeration признак ручной модерации заявок
 * @param eventDate дата и время проведения события
 */
public record EventInternalDto(
        Long id,
        Long initiatorId,
        Long categoryId,
        String title,
        String state,
        Integer participantLimit,
        Boolean requestModeration,
        LocalDateTime eventDate
) {
}
