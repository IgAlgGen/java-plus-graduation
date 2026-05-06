package ewm.user.service;

import ewm.common.exception.NotFoundException;
import ewm.user.client.UserClient;
import ewm.user.model.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.internal.dto.IdsRequest;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserReferenceService {

    private final UserClient userClient;
    private final EntityManager entityManager;

    public User getExistingReference(Long userId) {
        if (!exists(userId)) {
            throw new NotFoundException("User not found: " + userId);
        }
        return entityManager.getReference(User.class, userId);
    }

    public void ensureExists(Long userId) {
        if (!exists(userId)) {
            throw new NotFoundException("User not found: " + userId);
        }
    }

    private boolean exists(Long userId) {
        Map<Long, Boolean> result = userClient.existsUsers(new IdsRequest(List.of(userId)));
        return Boolean.TRUE.equals(result.get(userId));
    }
}
