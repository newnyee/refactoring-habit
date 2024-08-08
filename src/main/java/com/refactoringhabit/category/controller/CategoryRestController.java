package com.refactoringhabit.category.controller;

import com.refactoringhabit.category.domain.service.CategoryService;
import com.refactoringhabit.category.dto.CategoryLargeResponseDto;
import com.refactoringhabit.common.response.ApiResponse;
import com.refactoringhabit.product.domain.service.ProductService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/categories")
public class CategoryRestController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<CategoryLargeResponseDto>> getCategoriesApi() {
        return ApiResponse.ok(categoryService.getCategories());
    }

    @GetMapping("/{categoryName}/products")
    public ApiResponse<Map<String, Object>> getProductsByCategoryLargeApi(
        @PathVariable("categoryName") String categoryName,
        @RequestParam("order-by") String orderByValue,
        Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        map.put("products",
            productService.getProductsByCategoryLarge(categoryName, pageable, orderByValue));
        map.put("productsCount", productService.getProductCountByCategoryLarge(categoryName));
        return ApiResponse.ok(map);
    }
}
