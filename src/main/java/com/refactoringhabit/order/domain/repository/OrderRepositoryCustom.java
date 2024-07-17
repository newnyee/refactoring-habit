package com.refactoringhabit.order.domain.repository;

import com.refactoringhabit.order.dto.OrderSummaryByHostIdDto;
import com.refactoringhabit.order.dto.OrderSummaryByProductIdDto;

public interface OrderRepositoryCustom {
    OrderSummaryByProductIdDto orderSummaryByProductId(Long productId);
    OrderSummaryByHostIdDto orderSummaryByHostId(Long hostId);
    Long orderRefundCountByHostId(Long hostId);
}
