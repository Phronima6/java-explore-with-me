package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatDtoInput {

    @NotBlank
    @Size(max = 8000, message = "Идентификатор сервиса должен быть не более 8000 символов")
    String app; // Идентификатор сервиса для которого записывается информация
    @NotBlank
    @Size(max = 39, message = "Идентификатор записи должен быть не более 39 символов")
    String ip; // Идентификатор записи
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    LocalDateTime timestamp; // Дата и время, когда был совершен запрос (в формате "yyyy-MM-dd HH:mm:ss")
    @NotBlank
    @Size(max = 8000, message = "URI  должен быть не более 8000 символов")
    String uri; // URI для которого был осуществлен запрос

}