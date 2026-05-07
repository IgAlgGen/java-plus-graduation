package ewm.request.dto;

import lombok.*;

import java.util.List;

/**
 * Результат группового изменения статусов заявок на участие.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EventRequestStatusUpdateResult {
    /** Заявки, переведенные в статус подтверждения. */
    private List<ParticipationRequestDto> confirmedRequests;
    /** Заявки, переведенные в статус отклонения. */
    private List<ParticipationRequestDto> rejectedRequests;
}
