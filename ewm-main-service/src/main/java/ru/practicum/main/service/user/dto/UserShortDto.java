package ru.practicum.main.service.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserShortDto {

    Long id;
    String name;

}