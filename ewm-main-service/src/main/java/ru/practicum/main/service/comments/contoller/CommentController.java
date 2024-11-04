package ru.practicum.main.service.comments.contoller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.comments.dto.CommentRequestDto;
import ru.practicum.main.service.comments.dto.CommentResponseDto;
import ru.practicum.main.service.comments.dto.CommentResponseShortDto;
import ru.practicum.main.service.comments.service.CommentService;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
@Validated
public class CommentController {

    CommentService commentService;
    static final String COMMENT_ID = "comment-id";
    static final String EVENT_ID = "event-id";
    static final String USER_ID = "user-id";
    static final String PATH_COMMENT_ID = "/{comment-id}";
    static final String PATH_EVENT_ID = "/{event-id}";
    static final String PATH_USER_ID = "/{user-id}";

    @PostMapping(PATH_USER_ID + "/events" + PATH_EVENT_ID + "/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@RequestBody @Valid final CommentRequestDto commentRequestDto,
                                            @PathVariable(USER_ID) final Long userId,
                                            @PathVariable(EVENT_ID) final Long eventId) {
        return commentService.createComment(commentRequestDto, userId, eventId);
    }

    @DeleteMapping(PATH_USER_ID + "/events/comments" + PATH_COMMENT_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable(USER_ID) final Long userId,
                              @PathVariable(COMMENT_ID) final Long commentId) {
        commentService.deleteComment(userId, commentId);
    }

    @GetMapping("/events/comments" + PATH_COMMENT_ID)
    public CommentResponseDto getComment(@PathVariable(COMMENT_ID) final Long commentId) {
        return commentService.getComment(commentId);
    }

    @GetMapping("/events" + PATH_EVENT_ID + "/comments")
    public List<CommentResponseShortDto> getComments(@PathVariable(EVENT_ID) final Long eventId) {
        return commentService.getComments(eventId);
    }

    @PatchMapping(PATH_USER_ID + "/events/comments" + PATH_COMMENT_ID)
    public CommentResponseDto updateComment(@RequestBody @Valid final CommentRequestDto commentRequestDto,
                                            @PathVariable(USER_ID) final Long userId,
                                            @PathVariable(COMMENT_ID) final Long commentId) {
        return commentService.updateComment(commentRequestDto, userId, commentId);
    }

}