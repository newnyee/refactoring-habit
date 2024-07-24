package com.refactoringhabit.order.domain.repository;

import static com.refactoringhabit.order.domain.entity.QOrder.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.order.domain.enums.UsedStatus;
import com.refactoringhabit.order.dto.OrderSummaryByHostIdDto;
import com.refactoringhabit.order.dto.OrderSummaryByProductIdDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private static final String SALES_AMOUNT = "salesAmount";
    private static final String SALES_VOLUME = "salesVolume";

    @Override
    public OrderSummaryByProductIdDto orderSummaryByProductId(Long productId) {
        return jpaQueryFactory
            .select(Projections.constructor(OrderSummaryByProductIdDto.class,
                order.quantity.sum().castToNum(Long.class)
                    .coalesce(0L).as(SALES_AMOUNT),
                order.price.sum().castToNum(Long.class)
                    .coalesce(0L).as(SALES_VOLUME),
                order.price.min().as("minPrice"),
                order.price.max().as("maxPrice")))
            .from(order)
            .where(order.option.product.id.eq(productId)
                .and(order.usedStatus.eq(UsedStatus.USED)))
            .fetchFirst();
    }

    @Override
    public OrderSummaryByHostIdDto orderSummaryByHostId(Long hostId) {
        return jpaQueryFactory
            .select(Projections.constructor(OrderSummaryByHostIdDto.class,
                order.quantity.sum().castToNum(Long.class)
                    .coalesce(0L).as(SALES_AMOUNT),
                order.price.sum().castToNum(Long.class)
                    .coalesce(0L).as(SALES_VOLUME)))
            .from(order)
            .where(order.host.id.eq(hostId)
                .and(order.usedStatus.eq(UsedStatus.USED)))
            .fetchFirst();
    }

    @Override
    public Long orderRefundCountByHostId(Long hostId) {
        return jpaQueryFactory
            .select(order.quantity.sum().castToNum(Long.class)
                .coalesce(0L))
            .from(order)
            .where(order.host.id.eq(hostId)
                .and(order.usedStatus.eq(UsedStatus.CANCELED)))
            .fetchFirst();
    }

    @Override
    public OrderSummaryByHostIdDto orderSummaryByHostIdAndDate(Long hostId, LocalDate date) {
        LocalDateTime startOfDate = date.atStartOfDay();
        LocalDateTime endOfDate = date.atTime(LocalTime.MAX);

        return jpaQueryFactory
            .select(Projections.constructor(OrderSummaryByHostIdDto.class,
                order.quantity.sum().castToNum(Long.class)
                    .coalesce(0L).as(SALES_AMOUNT),
                order.price.sum().castToNum(Long.class)
                    .coalesce(0L).as(SALES_VOLUME)))
            .from(order)
            .where(order.host.id.eq(hostId)
                .and(order.createdAt.between(startOfDate, endOfDate))
                .and(order.usedStatus.eq(UsedStatus.USED)))
            .fetchFirst();
    }

    @Override
    public Long orderRefundCountByHostIdAndDate(Long hostId, LocalDate date) {
        LocalDateTime startOfDate = date.atStartOfDay();
        LocalDateTime endOfDate = date.atTime(LocalTime.MAX);

        return jpaQueryFactory
            .select(order.quantity.sum().castToNum(Long.class)
                .coalesce(0L))
            .from(order)
            .where(order.host.id.eq(hostId)
                .and(order.usedAt.between(startOfDate, endOfDate))
                .and(order.usedStatus.eq(UsedStatus.CANCELED)))
            .fetchFirst();
    }
}
