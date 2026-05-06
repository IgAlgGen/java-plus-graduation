package ru.practicum.ewm.internal.dto;

import java.time.LocalDateTime;

public record ParticipationRequestInternalDto(
        Long id,
        LocalDateTime created,
        Long eventId,
        Long requesterId,
        String status
) {
}
