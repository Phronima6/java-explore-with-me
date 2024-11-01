package ru.practicum.main.service.request.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.user.model.User;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "requests")
public class Request {

    LocalDateTime created; // Дата и время создания записи
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event; // Событие, связанное с заявкой
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // Уникальный идентификатор заявки
    @ManyToOne
    @JoinColumn(name = "requester_id")
    User requester; // Пользователь, подавший заявку
    @Enumerated(EnumType.STRING)
    RequestStatus status; // Статус заявки

}