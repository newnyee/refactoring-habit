package com.refactoringhabit.product.dto;

import java.math.BigDecimal;

public record ProductDetailDto(
    Long hostId,
    String name,
    String imageFileNames,
    String description,
    String zipCode,
    String address1,
    String address2,
    String extraAddress,
    String tagGender,
    String tagAge,
    String tagWith,
    int price,
    long reviewCount,
    BigDecimal reviewAverage,
    int wishCount) {
}
