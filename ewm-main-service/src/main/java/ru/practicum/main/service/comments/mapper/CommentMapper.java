package ru.practicum.main.service.comments.mapper;

import ru.practicum.main.service.comments.dto.CommentRequestDto;
import ru.practicum.main.service.comments.dto.CommentResponseDto;
import ru.practicum.main.service.comments.dto.CommentResponseShortDto;
import ru.practicum.main.service.comments.model.Comment;
import ru.practicum.main.service.event.mapper.EventMapper;
import ru.practicum.main.service.user.mapper.UserMapper;

public class CommentMapper {

    public static Comment toComment(final CommentRequestDto commentRequestDto) {
        final Comment comment = new Comment();
        comment.setTextContent(commentRequestDto.getTextContent());
        return comment;
    }

    public static CommentResponseDto toCommentResponseDto(final Comment comment) {
        final CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setCreatedOn(comment.getCreatedOn());
        commentResponseDto.setEventResponseShortDto(EventMapper.toEventResponseShortDto(comment.getEvent()));
        commentResponseDto.setTextContent(comment.getTextContent());
        commentResponseDto.setUserShortDto(UserMapper.toUserShortDto(comment.getUser()));
        return commentResponseDto;
    }

    public static CommentResponseShortDto toCommentResponseShortDto(final Comment comment) {
        final CommentResponseShortDto commentResponseShortDto = new CommentResponseShortDto();
        commentResponseShortDto.setCreatedOn(comment.getCreatedOn());
        commentResponseShortDto.setTextContent(comment.getTextContent());
        return commentResponseShortDto;
    }

}