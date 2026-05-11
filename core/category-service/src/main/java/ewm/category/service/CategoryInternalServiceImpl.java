package ewm.category.service;

import ewm.category.mapper.CategoryInternalMapper;
import ewm.category.model.Category;
import ewm.category.repository.CategoryRepository;
import ewm.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.internal.dto.CategoryInternalDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryInternalServiceImpl implements CategoryInternalService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public CategoryInternalDto getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + categoryId + " не найдена"));
        return CategoryInternalMapper.toInternalDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryInternalDto> getCategories(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return categoryRepository.findAllById(ids).stream()
                .map(CategoryInternalMapper::toInternalDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Boolean> existsCategories(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        Map<Long, Boolean> result = ids.stream()
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> false,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));

        categoryRepository.findAllById(result.keySet())
                .forEach(category -> result.put(category.getId(), true));

        return result;
    }
}
