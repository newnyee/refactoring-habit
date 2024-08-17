package com.refactoringhabit.product.controller;

import com.refactoringhabit.common.response.ApiResponse;
import com.refactoringhabit.host.domain.service.HostService;
import com.refactoringhabit.product.dto.ProductCardDto;
import com.refactoringhabit.product.domain.service.ProductService;
import com.refactoringhabit.product.dto.ProductDetailDto;
import com.refactoringhabit.product.dto.ProductResponseDto;
import com.refactoringhabit.review.domain.service.ReviewService;
import com.refactoringhabit.review.dto.ReviewDetailListByProductResponseDto;
import com.refactoringhabit.wish.domain.service.WishService;
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
    private final HostService hostService;
    private final ReviewService reviewService;
    private final WishService wishService;

    private static final int REVIEW_PAGE_SIZE = 8;

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
        Pageable pageable = Pageable.ofSize(REVIEW_PAGE_SIZE);
        ProductDetailDto productDetails = productService.getProductDetailsById(productAltId);
        return ApiResponse.ok(
            ProductResponseDto.builder()
                .productDetailDto(productDetails)
                .simpleHostInfoDto(hostService.getSimpleHostInfo(productDetails.hostId()))
                .reviewDetailDto(reviewService.getReviewDetailDtoListByProductId(productAltId, pageable))
                .wishAltId(wishService.getWishAltId(memberAltId, productAltId))
                .build());
    }

    @GetMapping("/{productId}/reviews")
    public ApiResponse<ReviewDetailListByProductResponseDto> getReviewsByProductIdApi(Pageable pageable,
        @PathVariable("productId") String productAltId,
        @RequestParam("order-by") String orderByValue) {
        return ApiResponse.ok(
            ReviewDetailListByProductResponseDto.builder()
                .simpleProductInfoDto(productService.getSimpleProductInfo(productAltId))
                .reviewCount(reviewService.getReviewCountByProductId(productAltId))
                .reviewDetailDtos(reviewService
                    .getReviewDetailDtoListByProductId(productAltId, pageable, orderByValue))
                .build());
    }
}
