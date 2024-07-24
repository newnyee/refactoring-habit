package com.refactoringhabit.review.domain.repository;

import static com.refactoringhabit.review.domain.entity.QReview.review;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.review.domain.enums.ReviewStatus;
import com.refactoringhabit.review.dto.ReviewSummaryDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private static final String REVIEW_COUNT = "reviewCount";
    private static final String REVIEW_AVERAGE = "reviewAverage";

    @Override
    public ReviewSummaryDto reviewSummaryByProductId(Long productId) {
        return jpaQueryFactory
        .select(Projections.constructor(
            ReviewSummaryDto.class,
            review.count().as(REVIEW_COUNT),
            review.starScore.avg().castToNum(BigDecimal.class)
                .coalesce(BigDecimal.valueOf(0)).as(REVIEW_AVERAGE)))
        .from(review)
        .where(review.option.product.id.eq(productId)
            .and(review.status.eq(ReviewStatus.SHOW)))
        .fetchFirst();
    }

    @Override
    public ReviewSummaryDto reviewSummaryByHostId(Long hostId) {
        if (review.option.product.host != null) {
            return jpaQueryFactory
                .select(Projections.constructor(
                    ReviewSummaryDto.class,
                    review.count().as(REVIEW_COUNT),
                    review.starScore.avg().castToNum(BigDecimal.class)
                        .coalesce(BigDecimal.valueOf(0)).as(REVIEW_AVERAGE)))
                .from(review)
                .where(review.option.product.host.id.eq(hostId)
                    .and(review.status.eq(ReviewStatus.SHOW)))
                .fetchFirst();
        }
        return new ReviewSummaryDto(0L, BigDecimal.valueOf(0));
    }

    @Override
    public ReviewSummaryDto reviewSummaryByHostIdAndDate(Long hostId, LocalDate date) {
        if (review.option.product.host != null) {
            LocalDateTime startOfDate = date.atStartOfDay();
            LocalDateTime endOfDate = date.atTime(LocalTime.MAX);

            return jpaQueryFactory
                .select(Projections.constructor(
                    ReviewSummaryDto.class,
                    review.count().as(REVIEW_COUNT),
                    review.starScore.avg().castToNum(BigDecimal.class)
                        .coalesce(BigDecimal.valueOf(0)).as(REVIEW_AVERAGE)))
                .from(review)
                .where(review.option.product.host.id.eq(hostId)
                    .and(review.createdAt.between(startOfDate, endOfDate))
                    .and(review.status.eq(ReviewStatus.SHOW)))
                .fetchFirst();
        }
        return new ReviewSummaryDto(0L, BigDecimal.valueOf(0));
    }
}
