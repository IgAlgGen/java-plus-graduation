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

    @NotBlank
    private String name;
}
