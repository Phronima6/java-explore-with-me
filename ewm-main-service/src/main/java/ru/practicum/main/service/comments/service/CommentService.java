package ru.practicum.main.service.comments.service;

import ru.practicum.main.service.comments.dto.CommentRequestDto;
import ru.practicum.main.service.comments.dto.CommentResponseDto;
import ru.practicum.main.service.comments.dto.CommentResponseShortDto;
import java.util.List;

public interface CommentService {

    CommentResponseDto createComment(final CommentRequestDto commentRequestDto,
                                     final Long userId,
                                     final Long eventId);

    void deleteComment(final Long userId,
                       final Long commentId);

    CommentResponseDto getComment(final Long commentId);

    List<CommentResponseShortDto> getComments(final Long eventId);

    CommentResponseDto updateComment(final CommentRequestDto commentRequestDto,
                                     final Long userId,
                                     final Long commentId);

}