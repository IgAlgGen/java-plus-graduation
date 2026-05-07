package ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.common.dto.LocationDto;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Запрос пользователя на создание события.
 */
@Data
public class NewEventDto {
    /** Заголовок события; обязателен, от 3 до 120 символов. */
    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    /** Краткая аннотация события; обязательна, от 20 до 2000 символов. */
    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    /** Полное описание события; обязательно, от 20 до 7000 символов. */
    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    /** Идентификатор существующей категории события. */
    @NotNull
    private Long category;

    /** Дата и время проведения в формате {@code yyyy-MM-dd HH:mm:ss}. */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /** Координаты места проведения; обязательны. */
    @NotNull
    private LocationDto location;

    /** Признак платного участия; если не задан, событие считается бесплатным. */
    private Boolean paid;

    /** Лимит участников; не может быть отрицательным, {@code 0} означает отсутствие лимита. */
    @Min(0)
    private Integer participantLimit;

    /** Признак ручной модерации заявок; если не задан, модерация включена. */
    private Boolean requestModeration;
}
