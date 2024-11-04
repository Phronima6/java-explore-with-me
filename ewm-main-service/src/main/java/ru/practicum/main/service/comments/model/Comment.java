package ru.practicum.main.service.comments.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.user.model.User;
import java.time.LocalDateTime;

@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor
@Setter
@Table(name = "comments")
public class Comment {

    @CreationTimestamp
    @Column(name = "created_on")
    LocalDateTime createdOn; // Дата и время создания комментария
    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Event event; //Событие к которому был оставлен комментарий
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // Идентификатор комментария
    @Column(name = "text_content")
    String textContent; // Содержание комментария
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    User user; // Пользователь, который оставил комментарий

}