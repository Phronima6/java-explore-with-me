package ru.practicum.main.service.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.service.comments.model.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(Long eventId);

    @Query("""
        SELECT c
        FROM Comment c
        JOIN FETCH c.event
        JOIN FETCH c.user
        WHERE c.id = :commentId
        """)
    Optional<Comment> findCommentWithEventAndUserById(@Param("commentId") Long commentId);

}