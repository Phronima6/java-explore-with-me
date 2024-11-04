package ru.practicum.main.service.compilation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.event.dto.EventResponseShortDto;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationResponseDto {

    List<EventResponseShortDto> events;
    Long id;
    Boolean pinned;
    String title;

}