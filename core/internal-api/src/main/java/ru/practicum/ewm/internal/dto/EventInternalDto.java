package ru.practicum.ewm.internal.dto;

import java.time.LocalDateTime;

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
