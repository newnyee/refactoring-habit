package com.refactoringhabit.order.dto;

public record OrderSummaryByProductIdDto (
    Long salesVolume,
    Long salesAmount,
    int minPrice,
    int maxPrice) {
}
