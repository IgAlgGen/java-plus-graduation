package ewm.category.service;

import ru.practicum.ewm.internal.dto.CategoryInternalDto;

import java.util.List;
import java.util.Map;

/**
 * Внутренний сервис чтения категорий для других микросервисов.
 */
public interface CategoryInternalService {
    /**
     * Возвращает категорию по идентификатору.
     *
     * @param categoryId идентификатор категории
     * @return внутреннее представление категории
     */
    CategoryInternalDto getCategory(Long categoryId);

    /**
     * Возвращает найденные категории из переданного списка.
     *
     * @param ids идентификаторы категорий
     * @return найденные категории
     */
    List<CategoryInternalDto> getCategories(List<Long> ids);

    /**
     * Проверяет существование категорий.
     *
     * @param ids идентификаторы категорий
     * @return карта {@code id -> существует}
     */
    Map<Long, Boolean> existsCategories(List<Long> ids);
}
