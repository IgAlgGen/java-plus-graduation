package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Запрос на сохранение посещения эндпоинта во внешнем сервисе статистики.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndpointHitDto {
    /** Технический идентификатор записи, заполняется сервером статистики. */
    private Long id;

    /** Имя приложения-источника; обязательное непустое значение. */
    @NotBlank
    private String app;

    /** Посещенный URI; обязательное непустое значение. */
    @NotBlank
    private String uri;

    /** IP-адрес клиента; обязательное непустое значение. */
    @NotBlank
    private String ip;

    /** Время обращения в формате {@code yyyy-MM-dd HH:mm:ss}; обязательное значение. */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
