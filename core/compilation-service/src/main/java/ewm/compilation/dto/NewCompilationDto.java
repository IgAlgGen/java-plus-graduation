package ewm.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

/**
 * Запрос администратора на создание подборки событий.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDto {

    @NotBlank
    @Size(max = 50)
    private String title;
    /** Признак закрепления подборки; если не задан, используется {@code false}. */
    private Boolean pinned;
    private Set<Long> events;
}
