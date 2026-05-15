package ewm.user.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    /** Имя пользователя. */
    private String name;
    /** Email пользователя. */
    private String email;
}
