package com.refactoringhabit.order.domain.repository;

import com.refactoringhabit.order.dto.OrderSummaryDto;
import java.time.LocalDate;

public interface OrderRepositoryCustom {
    OrderSummaryDto orderSummaryByProductId(Long productId);
    OrderSummaryDto orderSummaryByHostId(Long hostId);
    Long orderRefundCountByHostId(Long hostId);
    OrderSummaryDto orderSummaryByHostIdAndDate(Long hostId, LocalDate date);
    Long orderRefundCountByHostIdAndDate(Long hostId, LocalDate date);
}
