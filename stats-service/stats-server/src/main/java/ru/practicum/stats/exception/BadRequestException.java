package ru.practicum.stats.exception;

/**
 * Исключение для некорректных параметров запроса к сервису статистики.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
