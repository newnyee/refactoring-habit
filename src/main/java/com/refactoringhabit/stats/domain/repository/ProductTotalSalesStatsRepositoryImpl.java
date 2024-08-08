package com.refactoringhabit.stats.domain.repository;

import static com.refactoringhabit.product.domain.entity.QProduct.product;
import static com.refactoringhabit.stats.domain.entity.QProductTotalSalesStats.productTotalSalesStats;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductTotalSalesStatsRepositoryImpl implements ProductTotalSalesStatsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long getViewCount(String productAltId) {
        return jpaQueryFactory
            .select(productTotalSalesStats.reviewCount)
            .from(productTotalSalesStats)
            .join(product)
            .on(product.id.eq(productTotalSalesStats.productId))
            .where(product.altId.eq(productAltId))
            .fetchOne();
    }
}
