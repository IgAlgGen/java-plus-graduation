package ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Представление комментария во внешнем API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    /** Текст комментария. */
    private String text;
    /** Идентификатор автора. */
    private Long author;
    /** Идентификатор события. */
    private Long event;
    /** Статус модерации комментария. */
    private String status;

    /** Дата создания комментария. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /** Дата последнего обновления комментария. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;
}
