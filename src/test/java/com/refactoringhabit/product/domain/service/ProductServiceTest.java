package com.refactoringhabit.product.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import com.refactoringhabit.common.domain.repository.RedisRepository;
import com.refactoringhabit.product.dto.ProductCardDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisRepository redisRepository;

    private static final String CACHE_NAME_POPULAR = "popular-products";
    private static final String CACHE_NAME_NEW = "new-products";
    private static final String CACHE_KEY = "SimpleKey []";
    private static final int SEARCH_RESULT_LIMIT = 20;

    @Test
    void testGetPopularProductsWithCache() {
        Object beforeCacheValue = redisRepository.getCache(CACHE_NAME_POPULAR, CACHE_KEY);
        assertNull(beforeCacheValue);

        List<ProductCardDto> result = productService.getPopularProducts();
        List<ProductCardDto> afterCacheValue =
            (List<ProductCardDto>) redisRepository.getCache(CACHE_NAME_POPULAR, CACHE_KEY);

        assertEquals(SEARCH_RESULT_LIMIT, afterCacheValue.size(), result.size());
    }

    @Test
    void testGetNewProductsWithCache() {
        Object beforeCacheValue = redisRepository.getCache(CACHE_NAME_NEW, CACHE_KEY);
        assertNull(beforeCacheValue);

        List<ProductCardDto> result = productService.getNewProducts();
        List<ProductCardDto> afterCacheValue =
            (List<ProductCardDto>) redisRepository.getCache(CACHE_NAME_NEW, CACHE_KEY);

        assertEquals(SEARCH_RESULT_LIMIT, afterCacheValue.size(), result.size());
    }
}
