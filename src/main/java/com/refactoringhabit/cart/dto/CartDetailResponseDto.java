package com.refactoringhabit.cart.dto;

public record CartDetailResponseDto(
    String cartAltId,
    int cartQuantity,
    String productAltId,
    String productName,
    String productImages,
    String optionAltId,
    String optionName,
    int optionQuantity,
    int optionPrice) {
}
