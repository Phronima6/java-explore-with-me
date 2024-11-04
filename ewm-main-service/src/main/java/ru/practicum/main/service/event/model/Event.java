package ru.practicum.main.service.event.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.user.model.User;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "events")
public class Event {

    String annotation; // Аннотация события
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category; // Категория события
    @Column(name = "confirmed_requests")
    Integer confirmedRequests; // Подтвержденные запросы на участие
    @Column(name = "created_on")
    LocalDateTime createdOn; // Дата и время создания события
    String description; // Описание события
    @Column(name = "event_date")
    LocalDateTime eventDate; // Дата и время события
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // Уникальный идентификатор события
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator; // Инициатор события
    Double lat; // Широта местоположения события
    Double lon; // Долгота местоположения события
    Boolean paid; // Платное событие
    @Column(name = "participant_limit")
    Integer participantLimit; // Лимит участников
    @Column(name = "published_on")
    LocalDateTime publishedOn; // Дата и время публикации события
    @Column(name = "request_moderation")
    Boolean requestModeration; // Модерация заявок
    @Enumerated(EnumType.STRING)
    EventState state; // Состояние события
    String title; // Название события
    @Transient
    Integer views; // Количество просмотров события

}