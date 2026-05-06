package ewm.user.service;

import ewm.user.dto.NewUserRequest;
import ewm.user.dto.UserDto;
import ru.practicum.ewm.internal.dto.UserInternalDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto create(NewUserRequest request);

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    void delete(Long userId);

    UserInternalDto getInternalUser(Long userId);

    List<UserInternalDto> getInternalUsers(List<Long> ids);

    Map<Long, Boolean> existsUsers(List<Long> ids);
}
