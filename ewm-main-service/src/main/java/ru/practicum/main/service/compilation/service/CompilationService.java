package ru.practicum.main.service.compilation.service;

import ru.practicum.main.service.compilation.dto.CompilationRequestDto;
import ru.practicum.main.service.compilation.dto.CompilationResponseDto;
import ru.practicum.main.service.compilation.dto.CompilationUpdateDto;
import java.util.List;

public interface CompilationService {

    CompilationResponseDto createCompilation(final CompilationRequestDto compilationRequestDto);

    void deleteCompilation(final Long compilationId);

    CompilationResponseDto getCompilation(final Long compilationId);

    List<CompilationResponseDto> getCompilations(final Boolean pinned,
                                                 final int from,
                                                 final int size);

    CompilationResponseDto updateCompilation(final CompilationUpdateDto compilationUpdateDto,
                                             final Long compilationId);

}