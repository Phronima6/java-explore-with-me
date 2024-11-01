package ru.practicum.main.service.user.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "users")
public class User {

    String email; // Электронная почта пользователя
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // Идентификатор пользователя
    String name; // Имя пользователя

}