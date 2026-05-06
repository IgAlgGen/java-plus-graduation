package ewm.category.service;

import ru.practicum.ewm.internal.dto.CategoryInternalDto;

import java.util.List;
import java.util.Map;

public interface CategoryInternalService {
    CategoryInternalDto getCategory(Long categoryId);

    List<CategoryInternalDto> getCategories(List<Long> ids);

    Map<Long, Boolean> existsCategories(List<Long> ids);
}
