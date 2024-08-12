package com.refactoringhabit.product.dto;

import java.math.BigDecimal;

public record SimpleProductInfoDto(
    String name,
    BigDecimal reviewAverage) {
}
