package ru.practicum.main.service.comments.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponseShortDto {

    LocalDateTime createdOn;
    String textContent;

}