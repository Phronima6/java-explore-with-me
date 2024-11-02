package ru.practicum.main.service.user.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.user.dto.UserRequestDto;
import ru.practicum.main.service.user.dto.UserResponseDto;
import ru.practicum.main.service.user.service.UserService;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@RestController
@Validated
public class UserController {

    UserService userService;
    static final String USER_ID = "user-id";
    static final String PATH_USER_ID = "/{user-id}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid final UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @DeleteMapping(PATH_USER_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(USER_ID) final Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserResponseDto> getUsers(@RequestParam(required = false) final List<Long> ids,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                          @RequestParam(defaultValue = "10") @Positive final int size) {
        return userService.getUsers(ids, from, size);
    }

}