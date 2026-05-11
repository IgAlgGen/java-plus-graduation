package ewm.compilation.controller;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.dto.UpdateCompilationRequest;
import ewm.compilation.service.CompilationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Административный REST-контроллер подборок событий.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {

    private final CompilationService compilationService;

    /**
     * Создает подборку событий.
     *
     * @param dto данные подборки
     * @return созданная подборка
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto dto) {
        return compilationService.create(dto);
    }

    /**
     * Частично обновляет подборку событий.
     *
     * @param compId идентификатор подборки
     * @param dto изменяемые поля
     * @return обновленная подборка
     */
    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable Long compId,
                                 @Valid @RequestBody UpdateCompilationRequest dto) {
        return compilationService.update(compId, dto);
    }

    /**
     * Удаляет подборку событий.
     *
     * @param compId идентификатор подборки
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        compilationService.delete(compId);
    }
}
