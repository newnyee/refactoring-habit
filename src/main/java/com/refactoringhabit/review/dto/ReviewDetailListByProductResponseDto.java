package com.refactoringhabit.review.dto;

import com.refactoringhabit.product.dto.SimpleProductInfoDto;
import java.util.List;
import lombok.Builder;

@Builder
public record ReviewDetailListByProductResponseDto(
    SimpleProductInfoDto simpleProductInfoDto,
    Long reviewCount,
    List<ReviewDetailDto> reviewDetailDtos) {
}
