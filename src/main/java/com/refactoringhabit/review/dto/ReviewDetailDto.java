package com.refactoringhabit.review.dto;

import java.time.LocalDateTime;

public record ReviewDetailDto(
    String memberNickName,
    String memberProfileImage,
    String reviewAltId,
    String content,
    Integer starScore,
    String image,
    LocalDateTime updateAt,
    LocalDateTime createAt,
    String productName,
    String productAltId,
    String optionName) {
}
