package com.refactoringhabit.review.dto;

import com.refactoringhabit.product.dto.SimpleProductInfoDto;
import java.util.List;
import lombok.Builder;

@Builder
public record ReviewDetailListResponseDto(
    SimpleProductInfoDto simpleProductInfoDto,
    Long reviewCount,
    List<ReviewDetailDto> reviewDetailDtos) {
}
