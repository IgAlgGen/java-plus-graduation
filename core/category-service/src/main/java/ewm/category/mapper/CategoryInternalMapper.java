package ewm.category.mapper;

import ewm.category.model.Category;
import ru.practicum.ewm.internal.dto.CategoryInternalDto;

public class CategoryInternalMapper {
    public static CategoryInternalDto toInternalDto(Category category) {
        return new CategoryInternalDto(category.getId(), category.getName());
    }
}
