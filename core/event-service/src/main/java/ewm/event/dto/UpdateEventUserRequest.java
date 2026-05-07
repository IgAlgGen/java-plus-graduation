package ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.common.dto.LocationDto;
import ewm.event.model.EventStateAction;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Запрос инициатора на частичное обновление события.
 *
 * <p>Не заданные поля не изменяют текущее состояние события.</p>
 */
@Data
public class UpdateEventUserRequest {
    /** Новый заголовок события, от 3 до 120 символов. */
    @Size(min = 3, max = 120)
    private String title;

    /** Новая аннотация события, от 20 до 2000 символов. */
    @Size(min = 20, max = 2000)
    private String annotation;

    /** Новое описание события, от 20 до 7000 символов. */
    @Size(min = 20, max = 7000)
    private String description;
    /** Новый идентификатор категории. */
    private Long category;

    /** Новая дата события в формате {@code yyyy-MM-dd HH:mm:ss}. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /** Новые координаты места проведения. */
    private LocationDto location;
    /** Новый признак платного участия. */
    private Boolean paid;

    /** Новый лимит участников; не может быть отрицательным. */
    @Min(0)
    private Integer participantLimit;

    /** Новый признак ручной модерации заявок. */
    private Boolean requestModeration;
    /** Действие инициатора: отправить на модерацию или отменить рассмотрение. */
    private EventStateAction stateAction; // SEND_TO_REVIEW | CANCEL_REVIEW

    public boolean hasTitle() {
        return !(title == null || title.isBlank());
    }

    public boolean hasAnnotation() {
        return !(annotation == null || annotation.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasCategory() {
        return category != null;
    }

    public boolean hasEventDate() {
        return eventDate != null;
    }

    public boolean hasLocation() {
        return location != null;
    }

    public boolean hasPaid() {
        return paid != null;
    }

    public boolean hasParticipantLimit() {
        return participantLimit != null;
    }

    public boolean hasRequestModeration() {
        return requestModeration != null;
    }

    public boolean hasStateAction() {
        return stateAction != null;
    }
}
