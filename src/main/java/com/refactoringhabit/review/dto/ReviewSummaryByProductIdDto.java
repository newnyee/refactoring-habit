package com.refactoringhabit.review.dto;

import java.math.BigDecimal;

public record ReviewSummaryByProductIdDto(
    Long reviewCount,
    BigDecimal reviewAverage) {
}
