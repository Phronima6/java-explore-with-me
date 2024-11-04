package ru.practicum.main.service.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequestDto {

    @NotBlank
    @Size(max = 50, message = "Имя категории должно быть не более 50 символов.")
    String name;

}