package ewm.category.service;

import ewm.category.dto.CategoryDto;
import ewm.category.dto.NewCategoryDto;
import java.util.List;

/**
 * Сервис управления категориями событий.
 */
public interface CategoryService {
    /**
     * Создает категорию с уникальным именем без учета регистра.
     *
     * @param dto данные категории
     * @return созданная категория
     * @throws ewm.common.exception.ConflictException если имя уже занято
     */
    CategoryDto create(NewCategoryDto dto);

    /**
     * Обновляет имя категории.
     *
     * @param id идентификатор категории
     * @param dto новое имя категории
     * @return обновленная категория
     * @throws ewm.common.exception.NotFoundException если категория не найдена
     * @throws ewm.common.exception.ConflictException если новое имя уже занято
     */
    CategoryDto update(Long id, NewCategoryDto dto);

    /**
     * Удаляет категорию, если с ней не связаны события.
     *
     * @param id идентификатор категории
     * @throws ewm.common.exception.ConflictException если категория используется событиями
     */
    void delete(Long id);

    /**
     * Возвращает страницу категорий.
     *
     * @param from смещение первого результата
     * @param size размер страницы
     * @return список категорий
     */
    List<CategoryDto> findAll(int from, int size);

    /**
     * Возвращает категорию по идентификатору.
     *
     * @param id идентификатор категории
     * @return категория
     */
    CategoryDto findById(Long id);
}
