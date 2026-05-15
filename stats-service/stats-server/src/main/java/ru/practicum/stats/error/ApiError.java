package ru.practicum.stats.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Стандартное тело ответа об ошибке сервиса статистики.
 */
@Data
@Builder
public class ApiError {
    /** Подробные сообщения валидации или диагностики. */
    private List<String> errors;
    /** Пользовательское сообщение об ошибке. */
    private String message;
    /** Краткая причина ошибки. */
    private String reason;
    /** HTTP-статус ошибки в строковом виде. */
    private String status;
    /** Время формирования ответа об ошибке. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
