package ru.practicum.main.service.request.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.request.model.RequestStatus;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {

    LocalDateTime created;
    Long event;
    Long id;
    Long requester;
    RequestStatus status;

}