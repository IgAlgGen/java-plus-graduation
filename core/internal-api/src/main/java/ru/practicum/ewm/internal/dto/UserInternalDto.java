package ru.practicum.ewm.internal.dto;

/**
 * Внутреннее представление пользователя для межсервисных ответов.
 *
 * @param id идентификатор пользователя
 * @param name имя пользователя
 * @param email email пользователя
 */
public record UserInternalDto(
        Long id,
        String name,
        String email
) {
}
