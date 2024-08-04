package com.refactoringhabit.stats.domain.service;

import com.refactoringhabit.stats.domain.repository.ProductTotalSalesStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final ProductTotalSalesStatsRepository productTotalSalesStatsRepository;

    private static final String CACHE_NAME_VIEW_COUNT = "view-count";

    @Cacheable(value = CACHE_NAME_VIEW_COUNT, key = "productAltId", cacheManager = "redisCacheManager")
    public Long getCacheViewCount(String productAltId) {
        return productTotalSalesStatsRepository.getViewCount(productAltId);
    }
}
