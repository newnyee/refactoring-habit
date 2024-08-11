package com.refactoringhabit.review.dto;

public record ReviewDetailDto(
    String memberNickName,
    String memberProfileImage,
    String reviewAltId,
    String content,
    Integer starScore,
    String image) {
}
