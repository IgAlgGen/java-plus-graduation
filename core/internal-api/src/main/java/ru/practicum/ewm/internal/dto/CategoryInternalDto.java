package ru.practicum.ewm.internal.dto;

/**
 * Внутреннее представление категории для межсервисных ответов.
 *
 * @param id идентификатор категории
 * @param name название категории
 */
public record CategoryInternalDto(
        Long id,
        String name
) {
}
