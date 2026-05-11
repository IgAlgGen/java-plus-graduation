package ewm.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.internal.ApiPaths;
import ru.practicum.ewm.internal.dto.IdsRequest;
import ru.practicum.ewm.internal.dto.UserInternalDto;

import java.util.List;
import java.util.Map;

@FeignClient(name = "${user.service.id:user-service}")
public interface UserClient {

    @GetMapping(ApiPaths.USERS_BY_ID)
    UserInternalDto getUser(@PathVariable("userId") Long userId);

    @PostMapping(ApiPaths.USERS_BATCH)
    List<UserInternalDto> getUsers(@RequestBody IdsRequest request);

    @PostMapping(ApiPaths.USERS_EXISTS_BATCH)
    Map<Long, Boolean> existsUsers(@RequestBody IdsRequest request);
}
