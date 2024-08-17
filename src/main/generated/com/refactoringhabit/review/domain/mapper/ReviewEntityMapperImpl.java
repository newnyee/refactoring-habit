package com.refactoringhabit.review.domain.mapper;

import com.refactoringhabit.review.domain.entity.Review;
import com.refactoringhabit.review.dto.ReviewUpdateRequestDto;
import com.refactoringhabit.review.dto.ReviewUpdateResponseDto;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-16T18:25:28+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class ReviewEntityMapperImpl implements ReviewEntityMapper {

    @Override
    public ReviewUpdateResponseDto toReviewUpdateDto(Review review) {
        if ( review == null ) {
            return null;
        }

        int starScore = 0;
        String content = null;
        String image = null;

        starScore = review.getStarScore();
        content = review.getContent();
        image = review.getImage();

        ReviewUpdateResponseDto reviewUpdateResponseDto = new ReviewUpdateResponseDto( starScore, content, image );

        return reviewUpdateResponseDto;
    }

    @Override
    public void updateEntity(Review review, ReviewUpdateRequestDto reviewUpdateRequestDto, String imageFileNames) {
        if ( reviewUpdateRequestDto == null && imageFileNames == null ) {
            return;
        }

        if ( reviewUpdateRequestDto != null ) {
            review.setContent( reviewUpdateRequestDto.getContent() );
            review.setStarScore( reviewUpdateRequestDto.getStarScore() );
        }
        review.setImage( imageFileNames );
    }
}
