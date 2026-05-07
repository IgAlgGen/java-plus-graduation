package ewm.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

/**
 * Запрос администратора на частичное обновление подборки событий.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationRequest {
    /** Новый заголовок подборки до 50 символов. */
    @Size(max = 50)
    private String title;
    /** Новый признак закрепления подборки. */
    private Boolean pinned;
    /** Новый набор идентификаторов событий подборки. */
    private Set<Long> events;
}
