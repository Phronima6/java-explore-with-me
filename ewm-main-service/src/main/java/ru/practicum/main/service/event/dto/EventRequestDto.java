package ru.practicum.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.event.model.EventState;
import ru.practicum.main.service.event.model.Location;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestDto {

    @NotBlank
    @Size(min = 20, max = 2000, message = "Аннотация к событию должна быть не менее 20 и не более 2000 символов.")
    String annotation;
    @NotNull
    Long category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Описание к событию должно быть не менее 20 и не более 7000 символов.")
    String description;
    @NotNull
    @Future
    LocalDateTime eventDate;
    @NotNull
    Location location;
    @JsonSetter(nulls = Nulls.SKIP)
    Boolean paid = false;
    @PositiveOrZero
    @JsonSetter(nulls = Nulls.SKIP)
    Integer participantLimit = 0;
    @JsonSetter(nulls = Nulls.SKIP)
    Boolean requestModeration = true;
    EventState state;
    @NotBlank
    @Size(min = 3, max = 120, message = "Заголовок к событию должен быть не менее 3 и не более 120 символов.")
    String title;

}