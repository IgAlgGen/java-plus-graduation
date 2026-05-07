package ewm.request.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Запрос инициатора на изменение статуса заявок на участие в событии.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    /** Непустой список идентификаторов заявок, которые нужно обработать. */
    @NotEmpty
    private List<Long> requestIds;

    /** Целевое действие над заявками: подтверждение или отклонение. */
    @NotNull
    private RequestUpdateStatus status; // CONFIRMED / REJECTED

    /**
     * Доступные действия инициатора над заявками.
     */
    public enum RequestUpdateStatus {
        CONFIRMED, REJECTED
    }
}
