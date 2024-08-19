package com.refactoringhabit.product.dto;

public record OptionDetailDto(
    String altId,
    String name,
    int price,
    int quantity,
    String productType) {
}
