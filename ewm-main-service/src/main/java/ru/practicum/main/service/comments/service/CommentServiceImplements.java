package ru.practicum.main.service.comments.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.comments.dto.CommentRequestDto;
import ru.practicum.main.service.comments.dto.CommentResponseDto;
import ru.practicum.main.service.comments.dto.CommentResponseShortDto;
import ru.practicum.main.service.comments.mapper.CommentMapper;
import ru.practicum.main.service.comments.model.Comment;
import ru.practicum.main.service.comments.repository.CommentRepository;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.event.repository.EventRepository;
import ru.practicum.main.service.exception.EWMBadRequestException;
import ru.practicum.main.service.exception.EWMConflictException;
import ru.practicum.main.service.exception.EWMNotFoundException;
import ru.practicum.main.service.user.model.User;
import ru.practicum.main.service.user.repository.UserRepository;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImplements implements CommentService {

    CommentRepository commentRepository;
    EventRepository eventRepository;
    UserRepository userRepository;

    @Override
    @Transactional
    public CommentResponseDto createComment(final CommentRequestDto commentRequestDto,
                                            final Long userId,
                                            final Long eventId) {
        log.info("Добавление пользователем id {} комментария к событию id {}.", userId, eventId);
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EWMConflictException(String.format("Такого пользователя id %d нет.", userId)));
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EWMNotFoundException(String.format("Такого события id %d нет.", eventId)));
        final Comment comment = CommentMapper.toComment(commentRequestDto);
        comment.setEvent(event);
        comment.setUser(user);
        final CommentResponseDto commentResponseDto = CommentMapper.toCommentResponseDto(commentRepository.save(comment));
        log.info("Добавлен комментарий id {}.", comment.getId());
        return commentResponseDto;
    }

    @Override
    @Transactional
    public void deleteComment(final Long userId,
                              final Long commentId) {
        log.info("Удаление комментария id {}.", commentId);
        final Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new EWMNotFoundException(String.format("Такого комментария id %d нет.", commentId)));
        if (!comment.getUser().getId().equals(userId)) {
            throw new EWMBadRequestException("Комментарий не может быть удален другим пользователем.");
        }
        commentRepository.delete(comment);
        log.info("Комментарий id {} удалён.", commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDto getComment(final Long commentId) {
        log.info("Получение комментария id {}.", commentId);
        final Comment comment = commentRepository.findCommentWithEventAndUserById(commentId)
                .orElseThrow(() -> new EWMNotFoundException(String.format("Такого комментария id %d нет.", commentId)));
        log.info("Получен комментарий id {}.", commentId);
        return CommentMapper.toCommentResponseDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseShortDto> getComments(final Long eventId) {
        log.info("Получение списка комментариев к событию id {}.", eventId);
        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        log.info("Получен список комментариев к событию id {}.", eventId);
        return comments.stream()
                .map(CommentMapper::toCommentResponseShortDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(final CommentRequestDto commentRequestDto,
                                            final Long userId,
                                            final Long commentId) {
        log.info("Обновление пользователем id {} комментария id {}.", userId, commentId);
        final Comment comment = commentRepository.findCommentWithEventAndUserById(commentId)
                .orElseThrow(() -> new EWMNotFoundException(String.format("Такого комментария id %d нет.", commentId)));
        if (!comment.getUser().getId().equals(userId)) {
            throw new EWMConflictException("Комментарий не может быть обновлён другим пользователем.");
        }
        comment.setTextContent(commentRequestDto.getTextContent());
        commentRepository.save(comment);
        log.info("Комментарий id {} обновлён.", commentId);
        return CommentMapper.toCommentResponseDto(comment);
    }

}