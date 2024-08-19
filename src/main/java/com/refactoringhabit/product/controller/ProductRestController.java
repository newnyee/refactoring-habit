package com.refactoringhabit.product.controller;

import com.refactoringhabit.common.response.ApiResponse;
import com.refactoringhabit.product.dto.ProductCardDto;
import com.refactoringhabit.product.domain.service.ProductService;
import com.refactoringhabit.product.dto.ProductResponseDto;
import com.refactoringhabit.review.domain.service.ReviewService;
import com.refactoringhabit.review.dto.ReviewDetailListByProductResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/products")
public class ProductRestController {

    private final ProductService productService;
    private final ReviewService reviewService;

    @GetMapping("/popular")
    public ApiResponse<List<ProductCardDto>> popularProductsApi() {
        return ApiResponse.ok(productService.getPopularProducts());
    }

    @GetMapping("/new")
    public ApiResponse<List<ProductCardDto>> newProductsApi() {
        return ApiResponse.ok(productService.getNewProducts());
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponseDto> getProductByIdApi(
        @RequestAttribute(value = "memberAltId", required = false) String memberAltId,
        @PathVariable("productId") String productAltId) {
        return ApiResponse.ok(
            productService.getProductDetailsById(memberAltId, productAltId));
    }

    @GetMapping("/{productId}/reviews")
    public ApiResponse<ReviewDetailListByProductResponseDto> getReviewsByProductIdApi(
        @PathVariable("productId") String productAltId, Pageable pageable,
        @RequestParam("order-by") String orderByValue) {
        return ApiResponse.ok(
            reviewService.getReviewDetailDtoListByProductId(productAltId, pageable, orderByValue));
    }
}
