package com.refactoringhabit.product.domain.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.common.domain.repository.RedisRepository;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.product.dto.ProductCardDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
