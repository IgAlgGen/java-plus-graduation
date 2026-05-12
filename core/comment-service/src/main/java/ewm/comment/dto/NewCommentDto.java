package ewm.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Запрос пользователя на создание комментария.
 */
@Data
public class NewCommentDto {
    public NewCommentDto(String value) {
        this.text = value;
    }

    @NotNull
    @NotBlank
    @Size(min = 1, max = 5000)
    private String text;
}
