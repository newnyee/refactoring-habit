package com.refactoringhabit.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductCardDto(
    String productAltId,
    String name,
    String imageFileNames,
    Long reviewCount,
    BigDecimal reviewAverage,
    Integer minPrice) implements Serializable {
}
