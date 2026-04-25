package ru.practicum.user;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;

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