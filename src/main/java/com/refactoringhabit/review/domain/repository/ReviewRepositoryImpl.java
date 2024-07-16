package com.refactoringhabit.review.domain.repository;

import static com.refactoringhabit.review.domain.entity.QReview.review;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.review.domain.enums.ReviewStatus;
import com.refactoringhabit.review.dto.ReviewSummaryByProductIdDto;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ReviewSummaryByProductIdDto reviewSummaryByProductId(Long productId) {
        ReviewSummaryByProductIdDto reviewSummaryDto = jpaQueryFactory
        .select(Projections.constructor(
            ReviewSummaryByProductIdDto.class,
            review.count().as("reviewCount"),
            review.starScore.avg().castToNum(BigDecimal.class).as("reviewAverage")
        ))
        .from(review)
        .where(review.option.product.id.eq(productId)
            .and(review.status.eq(ReviewStatus.SHOW)))
        .fetchFirst();

        return reviewSummaryDto.reviewAverage() != null
            ? reviewSummaryDto
            : new ReviewSummaryByProductIdDto(0L, BigDecimal.valueOf(0));
    }
}
