package ewm.user.controller;

import ewm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.internal.ApiPaths;
import ru.practicum.ewm.internal.dto.IdsRequest;
import ru.practicum.ewm.internal.dto.UserInternalDto;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserInternalController {

    private final UserService userService;

    /**
     * Возвращает пользователя для межсервисного взаимодействия.
     *
     * @param userId идентификатор пользователя
     * @return внутреннее представление пользователя
     */
    @GetMapping(ApiPaths.USERS_BY_ID)
    public UserInternalDto getUser(@PathVariable("userId") Long userId) {
        return userService.getInternalUser(userId);
    }

    /**
     * Возвращает найденных пользователей из переданного набора идентификаторов.
     *
     * @param request запрос со списком идентификаторов
     * @return найденные пользователи
     */
    @PostMapping(ApiPaths.USERS_BATCH)
    public List<UserInternalDto> getUsers(@RequestBody IdsRequest request) {
        return userService.getInternalUsers(request.ids());
    }

    /**
     * Проверяет существование пользователей.
     *
     * @param request запрос со списком идентификаторов
     * @return карта {@code userId -> существует}
     */
    @PostMapping(ApiPaths.USERS_EXISTS_BATCH)
    public Map<Long, Boolean> existsUsers(@RequestBody IdsRequest request) {
        return userService.existsUsers(request.ids());
    }
}
