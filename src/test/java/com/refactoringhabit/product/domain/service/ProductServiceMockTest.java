package com.refactoringhabit.product.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.common.domain.repository.RedisRepository;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.product.dto.ProductCardDto;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ProductServiceMockTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RedisRepository redisRepository;

    @Mock
    private List<ProductCardDto> productDtos;

    @InjectMocks
    private ProductService productService;

    private static final String CACHE_NAME_POPULAR_PRODUCT = "popular-products";
    private static final String CACHE_NAME_NEW_PRODUCT = "new-products";
    private static final String CACHE_KEY_NAME = "SimpleKey []";
    private static final String CATEGORY_NAME = "categoryName";
    private static final String ORDER_BY_VALUE = "orderByValue";

    @Test
    void testUpdatePopularProductsCache() {
        when(productRepository.productsOrderBySalesVolumeAndReviewAverage())
            .thenReturn(productDtos);

        productService.updatePopularProductsCache();
        verify(redisRepository)
            .setCache(CACHE_NAME_POPULAR_PRODUCT, CACHE_KEY_NAME, productDtos);
    }

    @Test
    void testUpdateNewProductsCache() {
        when(productRepository.productsOrderByCreatedAt()).thenReturn(productDtos);

        productService.updateNewProductsCache();
        verify(redisRepository)
            .setCache(CACHE_NAME_NEW_PRODUCT, CACHE_KEY_NAME, productDtos);
    }

    @Test
    void testGetProductByCategoryLarge() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findByCategoryName(CATEGORY_NAME, pageable, ORDER_BY_VALUE))
            .thenReturn(Collections.emptyList());

        List<ProductCardDto> products =
            productService.getProductsByCategoryLarge(CATEGORY_NAME, pageable, ORDER_BY_VALUE);
        assertEquals(Collections.emptyList(), products);
    }

    @Test
    void testGetProductCountByCategoryLarge() {
        when(productRepository.countByCategoryName(CATEGORY_NAME))
            .thenReturn(0L);

        Long count = productService.getProductCountByCategoryLarge(CATEGORY_NAME);
        assertEquals(0L, count);
    }
}
