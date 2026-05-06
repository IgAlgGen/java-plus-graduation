package ru.practicum.ewm.internal.dto;

import java.util.List;

public record IdsRequest(
        List<Long> ids
) {
}
