package com.refactoringhabit.product.dto;

import com.refactoringhabit.host.dto.SimpleHostInfoDto;
import com.refactoringhabit.review.dto.ReviewDetailDto;
import java.util.List;
import lombok.Builder;

@Builder
public record ProductResponseDto(
    ProductDetailDto productDetailDto,
    List<OptionDetailDto> optionDetailDtos,
    SimpleHostInfoDto simpleHostInfoDto,
    List<ReviewDetailDto> reviewDetailDtos,
    String wishAltId) {
}
