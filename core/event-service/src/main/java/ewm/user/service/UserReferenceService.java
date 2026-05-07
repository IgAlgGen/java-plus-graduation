package ewm.user.service;

import ewm.common.exception.NotFoundException;
import ewm.user.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.internal.dto.IdsRequest;
import ru.practicum.ewm.internal.dto.UserInternalDto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserReferenceService {

    private final UserClient userClient;

    public void ensureExists(Long userId) {
        if (!exists(userId)) {
            throw new NotFoundException("User not found: " + userId);
        }
    }

    public Map<Long, UserInternalDto> getUsersByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        List<Long> distinctIds = userIds.stream()
                .distinct()
                .toList();

        return userClient.getUsers(new IdsRequest(distinctIds)).stream()
                .collect(Collectors.toMap(UserInternalDto::id, Function.identity()));
    }

    private boolean exists(Long userId) {
        Map<Long, Boolean> result = userClient.existsUsers(new IdsRequest(List.of(userId)));
        return Boolean.TRUE.equals(result.get(userId));
    }
}
