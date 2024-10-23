package ru.practicum.stats.server.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "stats")
public class Stat {

    @Column(name = "app")
    String app; // Идентификатор сервиса для которого записывается информация
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // Идентификатор записи
    @Column(name = "ip")
    String ip; // IP-адрес пользователя, осуществившего запрос
    @Column(name = "uri")
    String uri; // URI для которого был осуществлен запрос
    @Column(name = "time_stamp")
    LocalDateTime timestamp; // Дата и время, когда был совершен запрос

}