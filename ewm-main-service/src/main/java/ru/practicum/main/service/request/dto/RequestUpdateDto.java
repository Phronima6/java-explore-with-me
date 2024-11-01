package ru.practicum.main.service.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.request.model.RequestStatus;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateDto {

    @NotNull
    List<Long> requestIds;
    @NotNull
    RequestStatus status;

}