package ru.practicum.main.service.comments.dto;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequestDto {

    @Size(min = 6, max = 2000, message = "Комментарий должен быть не менее 6 и не более 2000 символов.")
    String textContent;

}