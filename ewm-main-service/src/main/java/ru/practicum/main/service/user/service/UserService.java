package ru.practicum.main.service.user.service;

import ru.practicum.main.service.user.dto.UserRequestDto;
import ru.practicum.main.service.user.dto.UserResponseDto;
import java.util.List;

public interface UserService {

    UserResponseDto createUser(final UserRequestDto userRequestDto);

    void deleteUser(final Long userId);

    List<UserResponseDto> getUsers(final List<Long> userIds,
                                   final int from,
                                   final int size);

}