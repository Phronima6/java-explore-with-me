package ru.practicum.main.service.user.mapper;

import ru.practicum.main.service.user.dto.UserRequestDto;
import ru.practicum.main.service.user.dto.UserResponseDto;
import ru.practicum.main.service.user.model.User;

public class UserMapper {

    public static User toUser(final UserRequestDto userRequestDto) {
        final User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        return user;
    }

    public static UserResponseDto toUserResponseDto(final User user) {
        final UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        return userResponseDto;
    }

}