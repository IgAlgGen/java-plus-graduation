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
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
