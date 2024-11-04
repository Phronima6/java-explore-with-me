package ru.practicum.main.service.category.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.category.dto.CategoryRequestDto;
import ru.practicum.main.service.category.dto.CategoryResponseDto;
import ru.practicum.main.service.category.service.CategoryService;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@RestController
@Validated
public class AdminCategoryController {

    CategoryService categoryService;
    static final String CATEGORY_ID = "cat-id";
    static final String PATH_CATEGORY_ID = "/{cat-id}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@Valid @RequestBody final CategoryRequestDto categoryRequestDto) {
        return categoryService.createCategory(categoryRequestDto);
    }

    @DeleteMapping(PATH_CATEGORY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(CATEGORY_ID) final Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @PatchMapping(PATH_CATEGORY_ID)
    public CategoryResponseDto updateCategory(@Valid @RequestBody final CategoryRequestDto categoryRequestDto,
                                              @PathVariable(CATEGORY_ID) @Positive final Long categoryId) {
        return categoryService.updateCategory(categoryRequestDto, categoryId);
    }

}