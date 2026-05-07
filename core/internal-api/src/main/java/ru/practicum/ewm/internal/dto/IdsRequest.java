package ru.practicum.ewm.internal.dto;

import java.util.List;

/**
 * Запрос внутреннего API со списком идентификаторов.
 *
 * @param ids идентификаторы сущностей
 */
public record IdsRequest(
        List<Long> ids
) {
}
