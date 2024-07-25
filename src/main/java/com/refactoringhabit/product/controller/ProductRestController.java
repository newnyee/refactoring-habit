package com.refactoringhabit.product.controller;

import com.refactoringhabit.common.response.ApiResponse;
import com.refactoringhabit.product.dto.HomeProductDto;
import com.refactoringhabit.product.domain.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/products")
public class ProductRestController {

    private final ProductService productService;

    @GetMapping("/popular")
    public ApiResponse<List<HomeProductDto>> popularProducts() {
        return ApiResponse.ok(productService.getPopularProducts());
    }

    @GetMapping("/new")
    public ApiResponse<List<HomeProductDto>> newProducts() {
        return ApiResponse.ok(productService.getNewProducts());
    }
}
