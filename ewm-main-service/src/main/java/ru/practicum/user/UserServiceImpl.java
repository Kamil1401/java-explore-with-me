package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public UserDto createUser(NewUserRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            throw new DuplicateException("Пользователь с таким email уже существует");
        }

        User user = UserMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        return UserMapper.toDto(savedUser);
    }


    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

        List<User> users = userRepository.findUsers(ids, pageable);

        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }


    public void deleteUser(Long userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
    }


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
