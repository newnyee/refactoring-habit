package com.refactoringhabit.order.dto;

public record OrderSummaryDto(
    Long salesAmount,
    Long salesVolume) {
}
