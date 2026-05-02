package ru.practicum.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User toEntity(NewUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());

        return user;
    }


    public static UserDto toDto(User user) {
        return new UserDto(user.getEmail(), user.getId(), user.getName());
    }


    public static UserShortDto toShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}