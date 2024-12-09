package ru.practicum.main.service.comments.service;

import jakarta.validation.constraints.NotNull;
import ru.practicum.main.service.comments.dto.CommentRequestDto;
import ru.practicum.main.service.comments.dto.CommentResponseDto;
import ru.practicum.main.service.comments.dto.CommentResponseShortDto;
import java.util.List;

public interface CommentService {

    CommentResponseDto createComment(final CommentRequestDto commentRequestDto,
                                     @NotNull final Long userId,
                                     @NotNull final Long eventId);

    void deleteComment(@NotNull final Long userId,
                       @NotNull final Long commentId);

    CommentResponseDto getComment(@NotNull final Long commentId);

    List<CommentResponseShortDto> getComments(@NotNull final Long eventId);

    CommentResponseDto updateComment(final CommentRequestDto commentRequestDto,
                                     @NotNull final Long userId,
                                     @NotNull final Long commentId);

}