package com.refactoringhabit.wish.domain.repository;

import static com.refactoringhabit.stats.domain.entity.QProductTotalSalesStats.productTotalSalesStats;
import static com.refactoringhabit.wish.domain.entity.QWish.wish;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.product.dto.ProductCardDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class WishRepositoryImpl implements WishRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<String> findAltIdByMemberAndProduct(Member member, Product product) {
        return Optional.ofNullable(
            jpaQueryFactory
            .select(wish.altId)
            .from(wish)
            .where(wish.member.eq(member).and(wish.product.eq(product)))
            .fetchOne());
    }

    @Override
    public List<ProductCardDto> getWishesByMember(Member member, Pageable pageable) {
        return jpaQueryFactory
            .select(Projections.constructor(ProductCardDto.class,
                wish.product.altId.as("productAltId"),
                wish.product.name,
                wish.product.imageFileNames,
                productTotalSalesStats.reviewCount,
                productTotalSalesStats.reviewAverage,
                productTotalSalesStats.minPrice))
            .from(wish).join(productTotalSalesStats)
            .on(wish.product.id.eq(productTotalSalesStats.productId))
            .where(wish.member.eq(member))
            .orderBy(wish.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }
}
