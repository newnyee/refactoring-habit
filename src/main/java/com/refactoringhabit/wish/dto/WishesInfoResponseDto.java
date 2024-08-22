package com.refactoringhabit.wish.dto;

import com.refactoringhabit.product.dto.ProductCardDto;
import java.util.List;
import lombok.Builder;

@Builder
public record WishesInfoResponseDto(
    List<ProductCardDto> wishes,
    Long wishCount) {
}
