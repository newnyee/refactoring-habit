package com.refactoringhabit.category.domain.service;

import com.refactoringhabit.category.domain.mapper.CategoryEntityMapper;
import com.refactoringhabit.category.domain.repository.CategoryLargeRepository;
import com.refactoringhabit.category.dto.CategoryLargeResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryLargeRepository categoryLargeRepository;

    @Cacheable(value = "categories", cacheManager = "redisCacheManager")
    public List<CategoryLargeResponseDto> getCategories() {
        return CategoryEntityMapper.INSTANCE
            .toCategoryLargeResponseDtoList(categoryLargeRepository.findAllWithCategoryMiddles());
    }
    
    @Cacheable(value = "categories-large", key = "#p0", cacheManager = "redisCacheManager")
    public CategoryLargeResponseDto getCategoryLarge(String categoryLargeName) {
        return CategoryEntityMapper.INSTANCE.toCategoryLargeResponseDto(
                categoryLargeRepository.findByEngName(categoryLargeName));
    }
}
