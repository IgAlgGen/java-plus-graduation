package ewm.category.controller;

import ewm.category.service.CategoryInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.internal.ApiPaths;
import ru.practicum.ewm.internal.dto.CategoryInternalDto;
import ru.practicum.ewm.internal.dto.IdsRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CategoryInternalController {

    private final CategoryInternalService categoryInternalService;

    @GetMapping(ApiPaths.CATEGORIES_BY_ID)
    public CategoryInternalDto getCategory(@PathVariable("categoryId") Long categoryId) {
        return categoryInternalService.getCategory(categoryId);
    }

    @PostMapping(ApiPaths.CATEGORIES_BATCH)
    public List<CategoryInternalDto> getCategories(@RequestBody IdsRequest request) {
        return categoryInternalService.getCategories(request.ids());
    }

    @PostMapping(ApiPaths.CATEGORIES_EXISTS_BATCH)
    public Map<Long, Boolean> existsCategories(@RequestBody IdsRequest request) {
        return categoryInternalService.existsCategories(request.ids());
    }
}
