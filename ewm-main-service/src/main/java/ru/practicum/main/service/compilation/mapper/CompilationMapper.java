package ru.practicum.main.service.compilation.mapper;

import ru.practicum.main.service.compilation.dto.CompilationRequestDto;
import ru.practicum.main.service.compilation.dto.CompilationResponseDto;
import ru.practicum.main.service.compilation.model.Compilation;
import ru.practicum.main.service.event.dto.EventResponseShortDto;
import ru.practicum.main.service.event.model.Event;
import java.util.List;

public class CompilationMapper {

    public static Compilation toCompilation(final CompilationRequestDto compilationRequestDto,
                                            final List<Event> evens) {
        final Compilation compilation = new Compilation();
        compilation.setEvents(evens);
        compilation.setPinned(compilationRequestDto.getPinned());
        compilation.setTitle(compilationRequestDto.getTitle());
        return compilation;
    }

    public static CompilationResponseDto toCompilationResponseDto(final Compilation compilation,
                                                                  final List<EventResponseShortDto> eventResponseShortDtos) {
        final CompilationResponseDto compilationResponseDto = new CompilationResponseDto();
        compilationResponseDto.setEvents(eventResponseShortDtos);
        compilationResponseDto.setId(compilation.getId());
        compilationResponseDto.setPinned(compilation.getPinned());
        compilationResponseDto.setTitle(compilation.getTitle());
        return compilationResponseDto;
    }

}