package ewm.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewUserRequest {
    /** Email пользователя; обязателен, должен быть валидным, от 6 до 254 символов. */
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;

    /** Имя пользователя; обязательное непустое значение от 2 до 250 символов. */
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
