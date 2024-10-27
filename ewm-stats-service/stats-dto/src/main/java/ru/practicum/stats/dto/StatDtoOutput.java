package ru.practicum.stats.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class StatDtoOutput {

    String app; // Название сервиса
    Long hits; // Количество просмотров
    String uri; // URI сервиса

}