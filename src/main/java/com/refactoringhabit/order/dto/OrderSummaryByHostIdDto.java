package com.refactoringhabit.order.dto;

public record OrderSummaryByHostIdDto (
    Long salesAmount,
    Long salesVolume) {
}
