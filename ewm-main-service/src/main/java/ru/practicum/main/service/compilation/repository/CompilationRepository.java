package ru.practicum.main.service.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.compilation.model.Compilation;
import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findByPinned(final Boolean pinned,
                                   final Pageable pageable);

}