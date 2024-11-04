package ru.practicum.main.service.request.contoller;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.request.dto.RequestDto;
import ru.practicum.main.service.request.service.RequestService;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/users/{user-id}/requests")
@RestController
@Validated
public class RequestController {

    RequestService requestService;
    static final String REQUEST_ID = "request-id";
    static final String PATH_REQUEST_ID = "/{request-id}";
    static final String PATH_USER_ID = "user-id";

    @PatchMapping(PATH_REQUEST_ID + "/cancel")
    public RequestDto cancelRequest(@PathVariable(PATH_USER_ID) @Positive final Long userId,
                                    @PathVariable(REQUEST_ID) @Positive final Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable(PATH_USER_ID) final Long userId,
                                    @RequestParam final Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getRequests(@PathVariable(PATH_USER_ID) @Positive final Long userId) {
        return requestService.getRequests(userId);
    }

}