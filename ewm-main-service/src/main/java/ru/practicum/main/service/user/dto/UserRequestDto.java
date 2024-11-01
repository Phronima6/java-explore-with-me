package ru.practicum.main.service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDto {

    @Email
    @NotBlank
    @Size(min = 6, max = 254, message = "Электронная почта пользователя должна быть не менее 6 и не более 254 символов.")
    String email;
    @NotBlank
    @Size(min = 2, max = 250, message = "Имя пользователя должно быть не менее 2 и не более 250 символов.")
    String name;

}