package com.refactoringhabit.review.domain.mapper;

import com.refactoringhabit.product.dto.SimpleProductInfoDto;
import com.refactoringhabit.review.domain.entity.Review;
import com.refactoringhabit.review.dto.ReviewDetailDto;
import com.refactoringhabit.review.dto.ReviewDetailListByProductResponseDto;
import com.refactoringhabit.review.dto.ReviewUpdateRequestDto;
import com.refactoringhabit.review.dto.ReviewUpdateResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewEntityMapper {

    ReviewEntityMapper INSTANCE = Mappers.getMapper(ReviewEntityMapper.class);

    ReviewUpdateResponseDto toReviewUpdateDto(Review review);

    @Mapping(target = "review.image", source = "imageFileNames")
    void updateEntity(@MappingTarget Review review, ReviewUpdateRequestDto reviewUpdateRequestDto, String imageFileNames);

    ReviewDetailListByProductResponseDto toReviewDetailListByProductResponseDto(
        SimpleProductInfoDto simpleProductInfoDto, Long reviewCount,
        List<ReviewDetailDto> reviewDetailDtos);
}
