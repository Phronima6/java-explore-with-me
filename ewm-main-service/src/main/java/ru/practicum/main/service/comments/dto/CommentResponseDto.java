package ru.practicum.main.service.comments.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.event.dto.EventResponseShortDto;
import ru.practicum.main.service.user.dto.UserShortDto;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CommentResponseDto {

    LocalDateTime createdOn;
    EventResponseShortDto eventResponseShortDto;
    String textContent;
    UserShortDto userShortDto;

}