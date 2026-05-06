package ewm.category.service;

import ewm.category.client.CategoryClient;
import ewm.category.model.Category;
import ewm.common.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.internal.dto.IdsRequest;
import ru.practicum.ewm.internal.dto.CategoryInternalDto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryReferenceService {

    private final CategoryClient categoryClient;
    private final EntityManager entityManager;

    public Category getExistingReference(Long categoryId) {
        if (!exists(categoryId)) {
            throw new NotFoundException("Category not found");
        }
        return entityManager.getReference(Category.class, categoryId);
    }

    public Map<Long, CategoryInternalDto> getCategoriesByIds(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Map.of();
        }

        List<Long> distinctIds = categoryIds.stream()
                .distinct()
                .toList();

        return categoryClient.getCategories(new IdsRequest(distinctIds)).stream()
                .collect(Collectors.toMap(CategoryInternalDto::id, Function.identity()));
    }

    private boolean exists(Long categoryId) {
        Map<Long, Boolean> result = categoryClient.existsCategories(new IdsRequest(List.of(categoryId)));
        return Boolean.TRUE.equals(result.get(categoryId));
    }
}
