package com.refactoringhabit.category.controller;

import com.refactoringhabit.category.domain.service.CategoryService;
import com.refactoringhabit.category.dto.CategoryLargeResponseDto;
import com.refactoringhabit.common.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ApiResponse<List<CategoryLargeResponseDto>> getCategoriesApi() {
        return ApiResponse.ok(categoryService.getCategories());
    }
}
