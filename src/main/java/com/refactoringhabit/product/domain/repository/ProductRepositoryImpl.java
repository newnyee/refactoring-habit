package com.refactoringhabit.product.domain.repository;

import static com.refactoringhabit.common.enums.AttributeNames.PRODUCT_ALT_ID;
import static com.refactoringhabit.product.domain.entity.QOption.option;
import static com.refactoringhabit.product.domain.entity.QProduct.product;
import static com.refactoringhabit.product.domain.enums.ProductStatus.OPENED;
import static com.refactoringhabit.stats.domain.entity.QProductTotalSalesStats.productTotalSalesStats;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.product.dto.ProductCardDto;
import com.refactoringhabit.product.dto.ProductSummaryDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

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
    public List<ProductCardDto> productsOrderBySalesVolumeAndReviewAverage() {
        return jpaQueryFactory
            .select(Projections.constructor(
                ProductCardDto.class,
                product.altId.as(PRODUCT_ALT_ID.getName()),
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
    public List<ProductCardDto> productsOrderByCreatedAt() {
        return jpaQueryFactory
            .select(Projections.constructor(
                ProductCardDto.class,
                product.altId.as(PRODUCT_ALT_ID.getName()),
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

    @Override
    public List<ProductCardDto> findByCategoryName(String categoryEngName, Pageable pageable) {
        return jpaQueryFactory
            .select(Projections.constructor(
                ProductCardDto.class,
                product.altId.as(PRODUCT_ALT_ID.getName()),
                product.name,
                product.imageFileNames,
                productTotalSalesStats.reviewCount,
                productTotalSalesStats.reviewAverage,
                productTotalSalesStats.minPrice))
            .from(product)
            .join(productTotalSalesStats)
            .on(product.id.eq(productTotalSalesStats.productId))
            .where(product.categoryMiddle.categoryLarge.engName.eq(categoryEngName)
                .or(product.categoryMiddle.engName.eq(categoryEngName))
                .and(product.status.eq(OPENED)))
            .orderBy(product.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public Long countByCategoryName(String categoryEngName) {
        return jpaQueryFactory
            .select(product.count())
            .from(product)
            .join(productTotalSalesStats)
            .on(product.id.eq(productTotalSalesStats.productId))
            .where(product.categoryMiddle.categoryLarge.engName.eq(categoryEngName)
                .or(product.categoryMiddle.engName.eq(categoryEngName))
                .and(product.status.eq(OPENED)))
            .orderBy(product.createdAt.desc())
            .fetchOne();
    }
}
