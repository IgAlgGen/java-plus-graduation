package ewm.user.dto;

import lombok.Data;

/**
 * Представление пользователя во внешнем административном API.
 */
@Data
public class UserDto {
    private Long id;
    /** Имя пользователя. */
    private String name;
    /** Email пользователя. */
    private String email;
}
