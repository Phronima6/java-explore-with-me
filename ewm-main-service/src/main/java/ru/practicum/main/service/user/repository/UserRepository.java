package ru.practicum.main.service.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.user.model.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIdIn(final List<Long> userIds,
                          final Pageable pageable);

}