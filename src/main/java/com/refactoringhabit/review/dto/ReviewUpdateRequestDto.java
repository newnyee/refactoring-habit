package com.refactoringhabit.review.dto;

import lombok.Getter;

@Getter
public class ReviewUpdateRequestDto {
    private int starScore;
    private String content;
    private String image;
}
