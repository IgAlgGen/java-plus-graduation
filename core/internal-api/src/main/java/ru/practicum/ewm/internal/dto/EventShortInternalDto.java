package ru.practicum.ewm.internal.dto;

import java.time.LocalDateTime;

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
