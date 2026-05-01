package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Transactional
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
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);


        List<User> users = userRepository.findAll(
                Specification.where(UserSpecifications.byIds(ids)), pageable)
                .getContent();

        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }


    @Transactional
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
