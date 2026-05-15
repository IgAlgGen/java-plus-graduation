package ewm.category.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.internal.ApiPaths;
import ru.practicum.ewm.internal.dto.CategoryInternalDto;
import ru.practicum.ewm.internal.dto.IdsRequest;

import java.util.List;
import java.util.Map;

@FeignClient(name = "${category.service.id:category-service}")
public interface CategoryClient {

    @GetMapping(ApiPaths.CATEGORIES_BY_ID)
    CategoryInternalDto getCategory(@PathVariable("categoryId") Long categoryId);

    @PostMapping(ApiPaths.CATEGORIES_BATCH)
    List<CategoryInternalDto> getCategories(@RequestBody IdsRequest request);

    @PostMapping(ApiPaths.CATEGORIES_EXISTS_BATCH)
    Map<Long, Boolean> existsCategories(@RequestBody IdsRequest request);
}
