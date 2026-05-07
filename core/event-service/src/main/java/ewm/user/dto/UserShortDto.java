package ewm.user.dto;

import lombok.Data;

/**
 * Краткое представление пользователя для вложения в ответы о событиях.
 */
@Data
public class UserShortDto {
    private Long id;
    /** Имя пользователя. */
    private String name;
}
