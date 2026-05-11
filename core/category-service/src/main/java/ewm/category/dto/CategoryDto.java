package ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Представление категории события во внешнем API.
 */
@Data
public class CategoryDto {
    @NotNull
    private Long id;

    /** Название категории; обязательное непустое значение. */
    @NotBlank
    private String name;
}
