package com.refactoringhabit.review.dto;

import java.math.BigDecimal;

public record ReviewSummaryDto(
    Long reviewCount,
    BigDecimal reviewAverage) {
}
