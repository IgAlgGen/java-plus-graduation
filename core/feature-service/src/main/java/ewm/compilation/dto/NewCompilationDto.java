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

    /** Заголовок подборки; обязательное непустое значение до 50 символов. */
    @NotBlank
    @Size(max = 50)
    private String title;
    /** Признак закрепления подборки; если не задан, используется {@code false}. */
    private Boolean pinned;
    /** Идентификаторы событий, включаемых в подборку. */
    private Set<Long> events;
}
