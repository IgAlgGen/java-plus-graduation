package ewm.compilation.service;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.dto.UpdateCompilationRequest;

import java.util.List;

/**
 * Сервис управления подборками событий.
 */
public interface CompilationService {
    /**
     * Создает подборку. Если список событий пуст, подборка создается без событий.
     *
     * @param dto данные новой подборки
     * @return созданная подборка
     */
    CompilationDto create(NewCompilationDto dto);

    /**
     * Частично обновляет подборку.
     *
     * @param compId идентификатор подборки
     * @param dto изменяемые поля
     * @return обновленная подборка
     * @throws ewm.common.exception.NotFoundException если подборка не найдена
     */
    CompilationDto update(Long compId, UpdateCompilationRequest dto);

    /**
     * Удаляет подборку.
     *
     * @param compId идентификатор подборки
     */
    void delete(Long compId);

    /**
     * Возвращает подборки с необязательным фильтром закрепления.
     *
     * @param pinned признак закрепления; {@code null} означает без фильтра
     * @param from смещение первого результата
     * @param size размер страницы
     * @return список подборок
     */
    List<CompilationDto> findAll(Boolean pinned, int from, int size);

    /**
     * Возвращает подборку по идентификатору.
     *
     * @param compId идентификатор подборки
     * @return подборка
     */
    CompilationDto findById(Long compId);
}
