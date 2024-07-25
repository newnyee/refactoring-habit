package com.refactoringhabit.category.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import com.refactoringhabit.category.domain.repository.CategoryLargeRepository;
import com.refactoringhabit.category.dto.CategoryLargeResponseDto;
import com.refactoringhabit.common.domain.repository.RedisRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private CategoryLargeRepository categoryLargeRepository;

    private static final String CACHE_NAME_CATEGORIES = "categories";
    private static final String CACHE_KEY = "SimpleKey []";

    @Test
    void testGetCategoriesWithCache() {
        long categoriesSize = categoryLargeRepository.count();

        Object beforeCacheValue =
            redisRepository.getCache(CACHE_NAME_CATEGORIES, CACHE_KEY);
        assertNull(beforeCacheValue);

        List<CategoryLargeResponseDto> result = categoryService.getCategories();
        List<CategoryLargeResponseDto> afterCacheValue = (List<CategoryLargeResponseDto>)
            redisRepository.getCache(CACHE_NAME_CATEGORIES, CACHE_KEY);

        assertEquals(categoriesSize, afterCacheValue.size(), result.size());
    }
}
