package com.refactoringhabit.review.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ReviewDetailListByMemberResponseDto(
    Long reviewCount,
    List<ReviewDetailDto> reviewDetailDtos) {
}
