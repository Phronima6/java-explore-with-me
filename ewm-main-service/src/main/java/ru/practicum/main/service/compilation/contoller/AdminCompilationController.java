package ru.practicum.main.service.compilation.contoller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.compilation.dto.CompilationRequestDto;
import ru.practicum.main.service.compilation.dto.CompilationResponseDto;
import ru.practicum.main.service.compilation.dto.CompilationUpdateDto;
import ru.practicum.main.service.compilation.service.CompilationService;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@RestController
@Validated
public class AdminCompilationController {

    CompilationService compilationService;
    static final String PATH_COMPILATION_ID = "comp-id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDto createCompilation(@Valid @RequestBody final CompilationRequestDto compilationRequestDto) {
        return compilationService.createCompilation(compilationRequestDto);
    }

    @DeleteMapping("/{" + PATH_COMPILATION_ID + "}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable(PATH_COMPILATION_ID) final Long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }

    @PatchMapping("/{" + PATH_COMPILATION_ID + "}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationResponseDto updateCompilation(@Valid @RequestBody CompilationUpdateDto compilationUpdateDto,
                                                    @PathVariable(PATH_COMPILATION_ID) final Long compilationId) {
        return compilationService.updateCompilation(compilationUpdateDto, compilationId);
    }

}