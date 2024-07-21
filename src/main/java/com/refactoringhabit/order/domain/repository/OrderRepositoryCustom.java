package com.refactoringhabit.order.domain.repository;

import com.refactoringhabit.order.dto.OrderSummaryByHostIdDto;
import com.refactoringhabit.order.dto.OrderSummaryByProductIdDto;
import java.time.LocalDate;

public interface OrderRepositoryCustom {
    OrderSummaryByProductIdDto orderSummaryByProductId(Long productId);
    OrderSummaryByHostIdDto orderSummaryByHostId(Long hostId);
    Long orderRefundCountByHostId(Long hostId);
    OrderSummaryByHostIdDto orderSummaryByHostIdAndDate(Long hostId, LocalDate date);
    Long orderRefundCountByHostIdAndDate(Long hostId, LocalDate date);
}
