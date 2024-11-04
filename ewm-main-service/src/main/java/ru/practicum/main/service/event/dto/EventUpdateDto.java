package ru.practicum.main.service.event.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.event.model.Location;
import ru.practicum.main.service.event.model.StateAction;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateDto {

    @Size(min = 20, max = 2000, message = "Аннотация к событию должна быть не менее 20 и не более 2000 символов.")
    String annotation;
    @PositiveOrZero
    Long category;
    @Size(min = 20, max = 7000, message = "Описание к событию должно быть не менее 20 и не более 7000 символов.")
    String description;
    @Future
    LocalDateTime eventDate;
    Location location;
    Boolean paid;
    @PositiveOrZero
    Integer participantLimit;
    Boolean requestModeration;
    StateAction stateAction;
    @Size(min = 3, max = 120, message = "Заголовок к событию должен быть не менее 3 и не более 120 символов.")
    String title;

}