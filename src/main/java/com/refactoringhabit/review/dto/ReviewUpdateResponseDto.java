package com.refactoringhabit.review.dto;

public record ReviewUpdateResponseDto(
    int starScore,
    String content,
    String image) {
}
