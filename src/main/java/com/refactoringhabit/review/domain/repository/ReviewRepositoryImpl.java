package com.refactoringhabit.review.domain.repository;

import static com.refactoringhabit.review.domain.entity.QReview.review;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.review.domain.enums.ReviewStatus;
import com.refactoringhabit.review.dto.ReviewDetailDto;
import com.refactoringhabit.review.dto.ReviewSummaryDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private static final String REVIEW_COUNT = "reviewCount";
    private static final String REVIEW_AVERAGE = "reviewAverage";
    private static final String ORDER_BY_VALUE_CREATE_AT = "createAt";
    private static final String ORDER_BY_VALUE_REVIEW_AVERAGE_DESC = "reviewAverageDesc";

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

    @Override
    public List<ReviewDetailDto> findByProductIdLimit(String productAltId, Pageable pageable,
        String orderByValue) {
        return jpaQueryFactory
            .select(Projections.constructor(ReviewDetailDto.class,
                review.member.nickName.as("memberNickName"),
                review.member.profileImage.as("memberProfileImage"),
                review.altId.as("reviewAltId"),
                review.content,
                review.starScore,
                review.image,
                review.updatedAt,
                review.createdAt,
                review.option.product.name.as("productName"),
                review.option.product.altId.as("productAltId"),
                review.option.name.as("optionName")))
            .from(review)
            .where(review.option.product.altId.eq(productAltId)
                .and(review.status.eq(ReviewStatus.SHOW)))
            .orderBy(getOrderSpecifier(orderByValue), review.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public Long countByProductId(String productAltId) {
        return jpaQueryFactory
            .select(review.count())
            .from(review)
            .where(review.option.product.altId.eq(productAltId)
                .and(review.status.eq(ReviewStatus.SHOW)))
            .orderBy(review.createdAt.desc())
            .fetchOne();
    }

    private OrderSpecifier<?> getOrderSpecifier(String orderByValue) {
        return switch (orderByValue) {
            case ORDER_BY_VALUE_CREATE_AT -> review.createdAt.desc();
            case ORDER_BY_VALUE_REVIEW_AVERAGE_DESC -> review.starScore.desc();
            default -> review.starScore.asc();
        };
    }
}
