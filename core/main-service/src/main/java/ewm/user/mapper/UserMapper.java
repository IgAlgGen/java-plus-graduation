package ewm.user.mapper;

import ewm.user.dto.UserShortDto;
import ewm.user.model.User;

public class UserMapper {

    public static UserShortDto toShortDto(User user) {
        UserShortDto dto = new UserShortDto();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        return dto;
    }
}
