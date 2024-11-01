package ru.practicum.main.service.event.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.category.dto.CategoryResponseDto;
import ru.practicum.main.service.user.dto.UserShortDto;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponseShortDto {

    String annotation;
    Boolean paid;
    CategoryResponseDto category;
    Integer confirmedRequests;
    LocalDateTime eventDate;
    Long id;
    UserShortDto initiator;
    String title;
    Integer views;

}