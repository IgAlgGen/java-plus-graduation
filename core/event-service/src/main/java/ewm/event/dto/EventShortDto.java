package ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.category.dto.CategoryDto;
import ewm.user.dto.UserShortDto;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Краткое представление события для списков во внешнем API.
 */
@Data
public class EventShortDto {
    private Long id;
    /** Заголовок события. */
    private String title;
    /** Краткая аннотация события. */
    private String annotation;
    /** Категория события. */
    private CategoryDto category;
    /** Количество подтвержденных заявок. */
    private Long confirmedRequests;

    /** Дата и время проведения события. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /** Инициатор события. */
    private UserShortDto initiator;
    /** Признак платного участия. */
    private Boolean paid;
    /** Количество просмотров по данным сервиса статистики. */
    private Long views;
}
