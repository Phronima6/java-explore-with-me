package ru.practicum.main.service.category.mapper;

import ru.practicum.main.service.category.dto.CategoryRequestDto;
import ru.practicum.main.service.category.dto.CategoryResponseDto;
import ru.practicum.main.service.category.model.Category;

public class CategoryMapper {

    public static Category toCategory(final CategoryRequestDto categoryRequestDto) {
        final Category category = new Category();
        category.setName(categoryRequestDto.getName());
        return category;
    }

    public static CategoryResponseDto toCategoryResponseDto(final Category category) {
        final CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setName(category.getName());
        return categoryResponseDto;
    }

}