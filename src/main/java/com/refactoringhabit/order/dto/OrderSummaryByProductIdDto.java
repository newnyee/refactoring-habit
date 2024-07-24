package com.refactoringhabit.order.dto;

public record OrderSummaryByProductIdDto (
    Long salesAmount,
    Long salesVolume,
    int minPrice,
    int maxPrice) {
}
