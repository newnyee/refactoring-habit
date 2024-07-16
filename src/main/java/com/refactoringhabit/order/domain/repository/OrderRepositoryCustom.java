package com.refactoringhabit.order.domain.repository;

import com.refactoringhabit.order.dto.OrderSummaryByProductIdDto;

public interface OrderRepositoryCustom {
    OrderSummaryByProductIdDto orderSummaryByProductId(Long productId);
}
