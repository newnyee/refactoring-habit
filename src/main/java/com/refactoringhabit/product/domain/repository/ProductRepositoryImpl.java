package com.refactoringhabit.product.domain.repository;

import static com.refactoringhabit.product.domain.entity.QOption.option;
import static com.refactoringhabit.product.domain.entity.QProduct.product;
import static com.refactoringhabit.product.domain.enums.ProductStatus.OPENED;
import static com.refactoringhabit.stats.domain.entity.QProductTotalSalesStats.productTotalSalesStats;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.product.dto.HomeProductDto;
import com.refactoringhabit.product.dto.ProductSummaryDto;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ProductSummaryDto findMinAndMaxProductPrice(Long productId) {
        return jpaQueryFactory
            .select(Projections.constructor(
                ProductSummaryDto.class,
                option.price.min().as("minPrice"),
                option.price.max().as("maxPrice")))
            .from(product)
            .join(option)
            .on(product.id.eq(option.product.id))
            .where(product.id.eq(productId))
            .fetchOne();
    }

    @Override
    public List<HomeProductDto> productsOrderBySalesVolumeAndReviewAverage() {
        return jpaQueryFactory
            .select(Projections.constructor(
                HomeProductDto.class,
                product.altId.as("productAltId"),
                product.name,
                product.imageFileNames,
                productTotalSalesStats.reviewCount,
                productTotalSalesStats.reviewAverage,
                productTotalSalesStats.minPrice))
            .from(product)
            .join(productTotalSalesStats)
            .on(product.id.eq(productTotalSalesStats.productId))
            .where(product.status.eq(OPENED))
            .orderBy(productTotalSalesStats.salesVolume
                    .multiply(productTotalSalesStats.reviewAverage).desc()
                , productTotalSalesStats.viewCount.desc())
            .limit(20)
            .fetch();
    }

    @Override
    public List<HomeProductDto> productsOrderByCreatedAt() {
        return jpaQueryFactory
            .select(Projections.constructor(
                HomeProductDto.class,
                product.altId.as("productAltId"),
                product.name,
                product.imageFileNames,
                productTotalSalesStats.reviewCount,
                productTotalSalesStats.reviewAverage,
                productTotalSalesStats.minPrice))
            .from(product)
            .join(productTotalSalesStats)
            .on(product.id.eq(productTotalSalesStats.productId))
            .where(product.status.eq(OPENED))
            .orderBy(product.createdAt.desc())
            .limit(20)
            .fetch();
    }
}
