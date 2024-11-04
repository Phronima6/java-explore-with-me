package ru.practicum.main.service.compilation.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.event.model.Event;
import java.util.List;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "compilations")
public class Compilation {

    @ManyToMany
    @JoinTable(name = "compilations_events",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "events_id")})
    List<Event> events; // Список событий, связанных с подборкой
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // Уникальный идентификатор подборки событий
    Boolean pinned; // Закреплённая подборка событий
    String title; // Заголовок подборки событий

}