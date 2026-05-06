package ewm.user.mapper;

import ewm.user.dto.NewUserRequest;
import ewm.user.dto.UserDto;
import ewm.user.model.User;
import ru.practicum.ewm.internal.dto.UserInternalDto;

public class UserMapper {

    public static User toEntity(NewUserRequest dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static UserInternalDto toInternalDto(User user) {
        return new UserInternalDto(user.getUserId(), user.getName(), user.getEmail());
    }
}
