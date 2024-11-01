package ru.practicum.main.service.compilation.contoller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.compilation.dto.CompilationResponseDto;
import ru.practicum.main.service.compilation.service.CompilationServiceImpl;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/compilations")
@RestController
@Validated
public class PublicCompilationController {

    CompilationServiceImpl compilationService;
    static final String PATH_COMPILATION_ID = "comp-id";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationResponseDto> getCompilations(@RequestParam(required = false) final Boolean pinned,
                                                        @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                                        @RequestParam(defaultValue = "10") @Positive final int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{" + PATH_COMPILATION_ID + "}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationResponseDto getCompilation(@PathVariable(PATH_COMPILATION_ID) final Long compId) {
        return compilationService.getCompilation(compId);
    }

}