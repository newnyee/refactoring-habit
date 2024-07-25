package com.refactoringhabit.product.domain.service;

import com.refactoringhabit.product.dto.HomeProductDto;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "popular-products", cacheManager = "contentCacheManager")
    public List<HomeProductDto> getPopularProducts() {
        return productRepository.productsOrderBySalesVolumeAndReviewAverage();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "new-products", cacheManager = "contentCacheManager")
    public List<HomeProductDto> getNewProducts() {
        return productRepository.productsOrderByCreatedAt();
    }
}
