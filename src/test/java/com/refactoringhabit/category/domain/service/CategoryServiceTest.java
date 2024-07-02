package com.refactoringhabit.category.domain.service;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.refactoringhabit.category.domain.entity.CategoryLarge;
import com.refactoringhabit.category.domain.repository.CategoryLargeRepository;
import com.refactoringhabit.category.dto.CategoryLargeResponseDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class CategoryServiceTest {

    @Mock
    private CategoryLargeRepository categoryLargeRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testGetCategoriesWithCache() {
        // Mock data
        CategoryLarge category1 = CategoryLarge.builder()
            .altId(randomUUID().toString())
            .engName("engName1")
            .name("categoryName1")
            .image("image.png1")
            .build();

        CategoryLarge category2 = CategoryLarge.builder()
            .altId(randomUUID().toString())
            .engName("engName2")
            .name("categoryName2")
            .image("image.png2")
            .build();

        List<CategoryLarge> mockCategories = List.of(category1, category2);
        when(categoryLargeRepository.findAll()).thenReturn(mockCategories);

        List<CategoryLargeResponseDto> result = categoryService.getCategories();
        assertEquals(2, result.size());
        assertEquals("categoryName1", result.get(0).getName());
        assertEquals("categoryName2", result.get(1).getName());
    }
}
