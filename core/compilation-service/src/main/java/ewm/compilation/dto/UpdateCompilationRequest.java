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
    @Size(max = 50)
    private String title;
    private Boolean pinned;
    private Set<Long> events;
}
