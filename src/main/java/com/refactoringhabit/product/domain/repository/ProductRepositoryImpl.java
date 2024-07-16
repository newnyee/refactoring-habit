package com.refactoringhabit.product.domain.repository;

import static com.refactoringhabit.product.domain.entity.QOption.option;
import static com.refactoringhabit.product.domain.entity.QProduct.product;
import static com.refactoringhabit.product.domain.enums.ProductStatus.OPENED;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.home.dto.HomeProductListDto;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<HomeProductListDto> productsOrderByCreatedAt() {
        return jpaQueryFactory
            .select(Projections.constructor(
                HomeProductListDto.class,
                product.altId.as("productAltId"),
                product.name,
                product.imageFileNames,
                ExpressionUtils.as(JPAExpressions
                    .select(option.price.min())
                    .from(option)
                    .where(option.product.id.eq(product.id)), "price")))
            .from(product)
            .where(product.status.eq(OPENED))
            .orderBy(product.createdAt.desc())
            .limit(20)
            .fetch();
    }
}
