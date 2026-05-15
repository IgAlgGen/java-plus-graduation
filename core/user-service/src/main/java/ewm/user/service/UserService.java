package ewm.user.service;

import ewm.user.dto.NewUserRequest;
import ewm.user.dto.UserDto;
import ru.practicum.ewm.internal.dto.UserInternalDto;

import java.util.List;
import java.util.Map;

/**
 * Сервис административных и внутренних операций с пользователями.
 */
public interface UserService {
    /**
     * Создает пользователя.
     *
     * @param request данные нового пользователя
     * @return созданный пользователь
     */
    UserDto create(NewUserRequest request);

    /**
     * Возвращает пользователей по списку идентификаторов или страницу всех пользователей.
     *
     * @param ids идентификаторы пользователей; пустой список означает всех пользователей
     * @param from смещение первого результата
     * @param size размер страницы
     * @return список пользователей
     */
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    /**
     * Удаляет пользователя.
     *
     * @param userId идентификатор пользователя
     * @throws ewm.common.exception.NotFoundException если пользователь не найден
     */
    void delete(Long userId);

    /**
     * Возвращает пользователя для внутреннего межсервисного API.
     *
     * @param userId идентификатор пользователя
     * @return внутреннее представление пользователя
     */
    UserInternalDto getInternalUser(Long userId);

    /**
     * Возвращает пользователей для внутреннего межсервисного API.
     *
     * @param ids идентификаторы пользователей
     * @return найденные пользователи
     */
    List<UserInternalDto> getInternalUsers(List<Long> ids);

    /**
     * Возвращает карту существования пользователей с сохранением порядка уникальных входных идентификаторов.
     *
     * @param ids идентификаторы пользователей
     * @return карта {@code id -> существует}
     */
    Map<Long, Boolean> existsUsers(List<Long> ids);
}
