package ru.practicum.ewm.internal.dto;

/**
 * Внутренний счетчик подтвержденных заявок по событию.
 *
 * @param eventId идентификатор события
 * @param confirmedRequests количество подтвержденных заявок
 */
public record EventConfirmedRequestsInternalDto(
        Long eventId,
        Long confirmedRequests
) {
}
