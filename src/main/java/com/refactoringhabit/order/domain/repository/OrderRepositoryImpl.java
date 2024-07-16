package com.refactoringhabit.order.domain.repository;

import static com.refactoringhabit.order.domain.entity.QOrder.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.order.domain.enums.UsedStatus;
import com.refactoringhabit.order.dto.OrderSummaryByProductIdDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public OrderSummaryByProductIdDto orderSummaryByProductId(Long productId) {
        OrderSummaryByProductIdDto orderSummaryDto = jpaQueryFactory
            .select(Projections.constructor(OrderSummaryByProductIdDto.class,
                order.price.sum().castToNum(Long.class).as("salesVolume"),
                order.count().as("salesAmount"),
                order.price.min().as("minPrice"),
                order.price.max().as("maxPrice")))
            .from(order)
            .where(order.option.product.id.eq(productId)
                .and(order.usedStatus.eq(UsedStatus.USED)))
            .fetchFirst();

        return orderSummaryDto.salesVolume() != null
            ? orderSummaryDto
            : new OrderSummaryByProductIdDto(0L, 0L, 0, 0);
    }
}
