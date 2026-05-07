package ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.category.dto.CategoryDto;
import ewm.common.dto.LocationDto;
import ewm.user.dto.UserShortDto;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Полное представление события во внешнем API.
 */
@Data
public class EventFullDto {
    private Long id;
    /** Заголовок события. */
    private String title;
    /** Краткая аннотация события. */
    private String annotation;
    /** Полное описание события. */
    private String description;
    /** Категория события. */
    private CategoryDto category;

    /** Дата создания события. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /** Дата и время проведения события. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /** Дата публикации события, если оно опубликовано. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    /** Инициатор события. */
    private UserShortDto initiator;
    /** Координаты места проведения. */
    private LocationDto location;
    /** Признак платного участия. */
    private Boolean paid;
    /** Лимит участников; {@code 0} означает отсутствие лимита. */
    private Integer participantLimit;
    /** Признак ручной модерации заявок. */
    private Boolean requestModeration;
    /** Текущее состояние события. */
    private String state;
    /** Количество подтвержденных заявок. */
    private Long confirmedRequests;
    /** Количество просмотров по данным сервиса статистики. */
    private Long views;
}
