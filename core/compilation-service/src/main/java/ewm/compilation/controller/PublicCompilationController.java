package ewm.compilation.controller;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.service.CompilationService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/compilations")
public class PublicCompilationController {

    private final CompilationService compilationService;

    /**
     * Возвращает подборки с необязательным фильтром закрепления.
     *
     * @param pinned признак закрепления; {@code null} означает без фильтра
     * @param from смещение первого результата
     * @param size размер страницы
     * @return список подборок
     */
    @GetMapping
    public List<CompilationDto> findAll(@RequestParam(required = false) Boolean pinned,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        return compilationService.findAll(pinned, from, size);
    }

    /**
     * Возвращает подборку по идентификатору.
     *
     * @param compId идентификатор подборки
     * @return подборка
     */
    @GetMapping("/{compId}")
    public CompilationDto findById(@PathVariable Long compId) {
        return compilationService.findById(compId);
    }
}
