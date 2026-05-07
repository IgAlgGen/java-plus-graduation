package ru.practicum.ewm.internal.dto;

import java.time.LocalDateTime;

/**
 * Внутреннее представление заявки на участие.
 *
 * @param id идентификатор заявки
 * @param created дата и время создания
 * @param eventId идентификатор события
 * @param requesterId идентификатор заявителя
 * @param status статус заявки
 */
public record ParticipationRequestInternalDto(
        Long id,
        LocalDateTime created,
        Long eventId,
        Long requesterId,
        String status
) {
}
