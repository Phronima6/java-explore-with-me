package ru.practicum.main.service.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.category.dto.CategoryRequestDto;
import ru.practicum.main.service.category.dto.CategoryResponseDto;
import ru.practicum.main.service.category.mapper.CategoryMapper;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.category.repository.CategoryRepository;
import ru.practicum.main.service.event.repository.EventRepository;
import ru.practicum.main.service.exception.EWMConflictException;
import ru.practicum.main.service.exception.EWMNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryServiceImplements implements CategoryService {

    CategoryRepository categoryRepository;
    EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryResponseDto createCategory(final CategoryRequestDto categoryRequestDto) {
        log.info("Добавление категории.");
        try {
            final Category category = categoryRepository.save(CategoryMapper.toCategory(categoryRequestDto));
            log.info("Категория добавлена {}.", category.getId());
            return CategoryMapper.toCategoryResponseDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new EWMConflictException("Категория с таким названием уже есть.");
        }
    }

    @Override
    @Transactional
    public void deleteCategory(final Long categoryId) {
        log.info("Удаление категории id {}.", categoryId);
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EWMNotFoundException("Такой категории id " + categoryId + " нет."));
        if (!eventRepository.findByCategoryId(categoryId).isEmpty()) {
            throw new EWMConflictException("Нельзя удалить категорию со связанными событиями.");
        }
        categoryRepository.delete(category);
        log.info("Категория id {} успешно удалена.", categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategory(final Long categoryId) {
        log.info("Получение категории id {}.", categoryId);
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EWMNotFoundException("Такой категории id " + categoryId + " нет."));
        return CategoryMapper.toCategoryResponseDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategories(final int from, final int size) {
        log.info("Получение всех категорий.");
        final PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        final List<Category> categories = categoryRepository.findAll(pageRequest).getContent();
        if (categories.isEmpty()) {
            log.info("Категорий нет.");
            return Collections.emptyList();
        }
        log.info("Категории найдены.");
        return categories.stream()
                .map(CategoryMapper::toCategoryResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto updateCategory(final CategoryRequestDto categoryRequestDto, final Long categoryId) {
        log.info("Обновление категории id {}.", categoryId);
        final Category oldCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EWMConflictException("Такой категории id " + categoryId + " нет."));
        try {
            oldCategory.setName(categoryRequestDto.getName());
            Category updatedCategory = categoryRepository.save(oldCategory);
            return CategoryMapper.toCategoryResponseDto(updatedCategory);
        } catch (DataIntegrityViolationException e) {
            throw new EWMConflictException("Нельзя изменить название категории, такая категория уже есть.");
        }
    }

}