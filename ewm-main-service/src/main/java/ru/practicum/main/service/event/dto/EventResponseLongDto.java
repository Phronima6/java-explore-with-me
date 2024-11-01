package ru.practicum.main.service.event.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.category.dto.CategoryResponseDto;
import ru.practicum.main.service.event.model.EventState;
import ru.practicum.main.service.event.model.Location;
import ru.practicum.main.service.user.dto.UserShortDto;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponseLongDto {

    String annotation;
    Boolean paid;
    CategoryResponseDto category;
    Integer confirmedRequests;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    Long id;
    UserShortDto initiator;
    Location location;
    Integer participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    EventState state;
    String title;
    Integer views;

}