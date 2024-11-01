package ru.practicum.main.service.compilation.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationRequestDto {

    List<Long> events;
    @JsonSetter(nulls = Nulls.SKIP)
    Boolean pinned = false;
    @NotBlank
    @Size(min = 1, max = 50, message = "Заголовок подборки должен быть не менее 1 и не более 50 символов.")
    String title;

}