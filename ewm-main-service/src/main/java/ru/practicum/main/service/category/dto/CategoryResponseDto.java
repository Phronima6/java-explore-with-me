package ru.practicum.main.service.category.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CategoryResponseDto {

    Long id;
    String name;

}