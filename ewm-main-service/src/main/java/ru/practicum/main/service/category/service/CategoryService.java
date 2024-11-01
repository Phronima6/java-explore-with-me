package ru.practicum.main.service.category.service;

import ru.practicum.main.service.category.dto.CategoryRequestDto;
import ru.practicum.main.service.category.dto.CategoryResponseDto;
import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(final CategoryRequestDto categoryRequestDto);

    void deleteCategory(final Long categoryId);

    CategoryResponseDto getCategory(final Long categoryId);

    List<CategoryResponseDto> getCategories(final int from,
                                            final int size);

    CategoryResponseDto updateCategory(final CategoryRequestDto categoryRequestDto,
                                       final Long categoryId);

}