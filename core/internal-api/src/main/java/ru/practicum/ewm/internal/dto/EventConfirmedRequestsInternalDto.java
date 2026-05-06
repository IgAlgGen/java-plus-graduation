package ru.practicum.ewm.internal.dto;

public record EventConfirmedRequestsInternalDto(
        Long eventId,
        Long confirmedRequests
) {
}
