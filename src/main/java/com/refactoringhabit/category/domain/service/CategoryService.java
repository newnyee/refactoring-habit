package com.refactoringhabit.category.domain.service;

import com.refactoringhabit.category.domain.mapper.CategoryEntityMapper;
import com.refactoringhabit.category.domain.repository.CategoryLargeRepository;
import com.refactoringhabit.category.dto.CategoryLargeResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryLargeRepository categoryLargeRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "categories", cacheManager = "redisCacheManager")
    public List<CategoryLargeResponseDto> getCategories() {
        return CategoryEntityMapper.INSTANCE
            .toCategoryLargeResponseDtoList(categoryLargeRepository.findAll());
    }
}
