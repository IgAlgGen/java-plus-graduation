package ewm.user.service;

import ewm.common.exception.NotFoundException;
import ewm.user.dto.NewUserRequest;
import ewm.user.dto.UserDto;
import ewm.user.mapper.UserMapper;
import ewm.user.model.User;
import ewm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.internal.dto.UserInternalDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(NewUserRequest request) {
        User user = UserMapper.toEntity(request);
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);

        List<User> users = (ids == null || ids.isEmpty())
                ? userRepository.findAll(page).getContent()
                : userRepository.findByUserIdIn(ids, page);

        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInternalDto getInternalUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
        return UserMapper.toInternalDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInternalDto> getInternalUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return userRepository.findByUserIdIn(ids).stream()
                .map(UserMapper::toInternalDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Boolean> existsUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        Map<Long, Boolean> result = ids.stream()
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> false,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));

        userRepository.findByUserIdIn(result.keySet().stream().toList())
                .forEach(user -> result.put(user.getUserId(), true));

        return result;
    }
}
