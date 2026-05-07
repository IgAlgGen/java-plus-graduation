package ewm.compilation.dto;

import ewm.event.dto.EventShortDto;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

/**
 * Представление подборки событий во внешнем API.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {
    private Long id;
    /** Заголовок подборки до 50 символов. */
    @Size(max = 50)
    private String title;
    /** Признак закрепления подборки. */
    private Boolean pinned;
    /** События, входящие в подборку. */
    private Set<EventShortDto> events;
}
