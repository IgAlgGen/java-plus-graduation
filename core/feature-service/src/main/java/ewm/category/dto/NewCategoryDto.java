package ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Запрос на создание или обновление категории.
 */
@Data
public class NewCategoryDto {
    /** Название категории; обязательное непустое значение от 1 до 50 символов. */
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
