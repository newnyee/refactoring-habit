package com.refactoringhabit.product.domain.service;

import com.refactoringhabit.common.domain.repository.RedisRepository;
import com.refactoringhabit.product.dto.ProductCardDto;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.product.dto.ProductDetailDto;
import com.refactoringhabit.product.dto.SimpleProductInfoDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RedisRepository redisRepository;

    private static final String CACHE_NAME_POPULAR_PRODUCT = "popular-products";
    private static final String CACHE_NAME_NEW_PRODUCT = "new-products";
    private static final String CACHE_KEY_NAME = "SimpleKey []";

    @Cacheable(value = CACHE_NAME_POPULAR_PRODUCT, cacheManager = "redisCacheManager")
    public List<ProductCardDto> getPopularProducts() {
        return productRepository.productsOrderBySalesVolumeAndReviewAverage();
    }

    @Cacheable(value = CACHE_NAME_NEW_PRODUCT, cacheManager = "redisCacheManager")
    public List<ProductCardDto> getNewProducts() {
        return productRepository.productsOrderByCreatedAt();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void updatePopularProductsCache() {
        log.info("Updating popular products cache ...");
        List<ProductCardDto> popularProducts =
            productRepository.productsOrderBySalesVolumeAndReviewAverage();
        redisRepository.setCache(CACHE_NAME_POPULAR_PRODUCT, CACHE_KEY_NAME, popularProducts);
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void updateNewProductsCache() {
        log.info("Updating new products cache ...");
        List<ProductCardDto> newProducts =
            productRepository.productsOrderByCreatedAt();
        redisRepository.setCache(CACHE_NAME_NEW_PRODUCT, CACHE_KEY_NAME, newProducts);
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> getProductsByCategoryLarge(String categoryName, Pageable pageable,
        String orderByValue) {
        return productRepository.findByCategoryName(categoryName, pageable, orderByValue);
    }

    @Transactional(readOnly = true)
    public Long getProductCountByCategoryLarge(String categoryName) {
        return productRepository.countByCategoryName(categoryName);
    }

    @Transactional(readOnly = true)
    public ProductDetailDto getProductDetailsById(String productAltId) {
        return productRepository.getProductDetailsByAltId(productAltId);
    }

    @Transactional(readOnly = true)
    public SimpleProductInfoDto getSimpleProductInfo(String productAltId) {
        return productRepository.getSimpleProductInfoByAltId(productAltId);
    }
}
