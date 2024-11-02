package ru.practicum.main.service.category.contoller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.category.dto.CategoryResponseDto;
import ru.practicum.main.service.category.service.CategoryService;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController
@Validated
public class PublicCategoryController {

    CategoryService categoryService;
    static final String CATEGORY_ID = "cat-id";
    static final String PATH_CATEGORY_ID = "/{cat-id}";

    @GetMapping
    public List<CategoryResponseDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                                   @RequestParam(defaultValue = "10") @Positive final int size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping(PATH_CATEGORY_ID)
    public CategoryResponseDto getCategory(@PathVariable(CATEGORY_ID) @Positive final Long categoryId) {
        return categoryService.getCategory(categoryId);
    }

}