package ewm.category.service;

import ewm.category.client.CategoryClient;
import ewm.common.exception.NotFoundException;
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

    public void ensureExists(Long categoryId) {
        if (!exists(categoryId)) {
            throw new NotFoundException("Category not found");
        }
    }
}
