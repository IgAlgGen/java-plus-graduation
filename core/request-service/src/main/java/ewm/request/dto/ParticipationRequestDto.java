package ewm.request.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Представление заявки на участие во внешнем API.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationRequestDto {
    private Long id;
    /** Дата и время создания заявки. */
    private LocalDateTime created;
    /** Идентификатор события. */
    private Long event;
    /** Идентификатор заявителя. */
    private Long requester;
    /** Текущий статус заявки. */
    private String status;
}
