package ru.practicum.main.service.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.exception.EWMConflictException;
import ru.practicum.main.service.exception.EWMNotFoundException;
import ru.practicum.main.service.user.dto.UserRequestDto;
import ru.practicum.main.service.user.dto.UserResponseDto;
import ru.practicum.main.service.user.mapper.UserMapper;
import ru.practicum.main.service.user.model.User;
import ru.practicum.main.service.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImplements implements UserService {

    UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDto createUser(final UserRequestDto userRequestDto) {
        log.info("Добавление пользователя.");
        try {
            final User user = userRepository.save(UserMapper.toUser(userRequestDto));
            log.info("Пользователь успешно добавлен id {}.", user.getId());
            return UserMapper.toUserResponseDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new EWMConflictException("Такой адрес электронной почты уже есть.");
        }
    }

    @Override
    @Transactional
    public void deleteUser(final Long userId) {
        log.info("Удаление пользователя id {}.", userId);
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EWMNotFoundException(String.format("Пользователя с id %d нет.", userId)));
        userRepository.delete(user);
        log.info("Пользователь id {} успешно удален.", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsers(final List<Long> userIds,
                                          final int from,
                                          final int size) {
        log.info("Получение списка пользователей.");
        final Pageable pageable = PageRequest.of(from / size, size);
        final List<User> users = (Objects.isNull(userIds) || userIds.isEmpty())
                ? userRepository.findAll(pageable).getContent()
                : userRepository.findByIdIn(userIds, pageable);
        log.info("Получен список {} пользователей", users.isEmpty() ? "всех" : "по заданным id.");
        return users.stream()
                .map(UserMapper::toUserResponseDto)
                .toList();
    }

}