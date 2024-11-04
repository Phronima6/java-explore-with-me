package ru.practicum.main.service.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.compilation.dto.CompilationRequestDto;
import ru.practicum.main.service.compilation.dto.CompilationResponseDto;
import ru.practicum.main.service.compilation.dto.CompilationUpdateDto;
import ru.practicum.main.service.compilation.mapper.CompilationMapper;
import ru.practicum.main.service.compilation.model.Compilation;
import ru.practicum.main.service.compilation.repository.CompilationRepository;
import ru.practicum.main.service.event.mapper.EventMapper;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.event.repository.EventRepository;
import ru.practicum.main.service.exception.EWMNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationServiceImplements implements CompilationService {

    CompilationRepository compilationRepository;
    EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationResponseDto createCompilation(final CompilationRequestDto compilationRequestDto) {
        log.info("Запрос на сохранение подборки администратором");
        final List<Event> events = eventRepository.findByIdIn(compilationRequestDto.getEvents());
        final Compilation compilation = CompilationMapper.toCompilation(compilationRequestDto, events);
        if (Objects.isNull(compilationRequestDto.getPinned())) {
            compilation.setPinned(false);
        }
        Compilation newCompilation = compilationRepository.save(compilation);
        log.info("Подборка успешно сохранена администратором");
        return CompilationMapper.toCompilationResponseDto(newCompilation,
                events.stream()
                        .map(EventMapper::toEventResponseShortDto)
                        .toList());
    }

    @Override
    @Transactional
    public void deleteCompilation(final Long compilationId) {
        log.info("Удаление подборки событий id {}.", compilationId);
        compilationRepository.findById(compilationId)
                .orElseThrow(() -> new EWMNotFoundException("Такой подборки событий id " + compilationId + " нет."));
        compilationRepository.deleteById(compilationId);
        log.info("Подборка id {} успешно удалена.", compilationId);
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationResponseDto getCompilation(final Long compilationId) {
        log.info("Запрос на получение данных подборки с id = {}", compilationId);
        final Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new EWMNotFoundException("Такой подборки событий id " + compilationId + " нет."));
        log.info("Подборка id {} получена.", compilationId);
        return CompilationMapper.toCompilationResponseDto(compilation,
                compilation.getEvents().stream()
                        .map(EventMapper::toEventResponseShortDto)
                        .toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationResponseDto> getCompilations(final Boolean pinned, final int from, final int size) {
        log.info("Получение всех подборок событий.");
        final Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Compilation> compilationList = Objects.nonNull(pinned) ?
                compilationRepository.findByPinned(pinned, pageable) :
                compilationRepository.findAll(pageable).toList();
        if (compilationList.isEmpty()) {
            log.info("Подборок событий нет.");
            return Collections.emptyList();
        }
        log.info("Найдены подборки событий.");
        return compilationList.stream()
                .map(c -> CompilationMapper.toCompilationResponseDto(c,
                        c.getEvents().stream()
                                .map(EventMapper::toEventResponseShortDto)
                                .toList()))
                .toList();
    }

    @Override
    @Transactional
    public CompilationResponseDto updateCompilation(final CompilationUpdateDto compilationUpdateDto, final Long compilationId) {
        log.info("Обновление подборки событий id {}.", compilationId);
        final Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new EWMNotFoundException("Такой подборки событий id " + compilationId + " нет."));
        final List<Event> events = eventRepository.findByIdIn(compilationUpdateDto.getEvents());
        if (Objects.nonNull(compilationUpdateDto.getEvents())) {
            compilation.setEvents(events);
        }
        if (Objects.nonNull(compilationUpdateDto.getPinned())) {
            compilation.setPinned(compilationUpdateDto.getPinned());
        }
        if (Objects.nonNull(compilationUpdateDto.getTitle())) {
            compilation.setTitle(compilationUpdateDto.getTitle());
        }
        Compilation updatedCompilation = compilationRepository.save(compilation);
        log.info("Подборка событий обновлена id {}.", compilationId);
        return CompilationMapper.toCompilationResponseDto(updatedCompilation,
                events.stream()
                        .map(EventMapper::toEventResponseShortDto)
                        .toList());
    }

}