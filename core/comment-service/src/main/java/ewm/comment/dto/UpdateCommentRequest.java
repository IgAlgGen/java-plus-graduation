package ewm.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Запрос пользователя на обновление комментария.
 */
@Data
public class UpdateCommentRequest {
    public UpdateCommentRequest(String value) {
        this.text = value;
    }

    @NotBlank
    @Size(min = 1, max = 5000)
    private String text;

    public boolean hasText() {
        return !(text == null || text.isBlank());
    }
}
