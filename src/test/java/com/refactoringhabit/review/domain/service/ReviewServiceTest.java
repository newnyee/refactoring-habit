package com.refactoringhabit.review.domain.service;

import static com.refactoringhabit.common.enums.AttributeNames.PRODUCT_ALT_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.refactoringhabit.review.domain.repository.ReviewRepository;
import com.refactoringhabit.review.dto.ReviewDetailDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("상품 아이디에 따른 리뷰 목록 얻기 - two parameters")
    void testGetReviewDetailDtoListByProductId_TwoParameters() {
        List<ReviewDetailDto> reviewDetailDtoList = mock(List.class);
        Pageable pageable = mock(Pageable.class);
        String orderByValue = "createAt";
        when(reviewRepository
            .findByProductIdLimit(PRODUCT_ALT_ID.getName(), pageable, orderByValue))
            .thenReturn(reviewDetailDtoList);

        List<ReviewDetailDto> getReviewDetailDtoList = reviewService.getReviewDetailDtoList(
            PRODUCT_ALT_ID.getName(), pageable);
        assertEquals(reviewDetailDtoList, getReviewDetailDtoList);
    }

    @Test
    @DisplayName("상품 아이디에 따른 리뷰 목록 얻기 - three parameters")
    void testGetReviewDetailDtoListByProductId_ThreeParameters() {
        List<ReviewDetailDto> reviewDetailDtoList = mock(List.class);
        Pageable pageable = mock(Pageable.class);
        String orderByValue = "createAt";
        when(reviewRepository
            .findByProductIdLimit(PRODUCT_ALT_ID.getName(), pageable, orderByValue))
            .thenReturn(reviewDetailDtoList);

        List<ReviewDetailDto> getReviewDetailDtoList = reviewService.getReviewDetailDtoList(
            PRODUCT_ALT_ID.getName(), pageable, orderByValue);
        assertEquals(reviewDetailDtoList, getReviewDetailDtoList);
    }

    @Test
    @DisplayName("상품 아이디에 따른 리뷰 수 얻기")
    void testGetReviewCount() {
        Long reviewCount = 10L;
        when(reviewRepository.countByProductId(PRODUCT_ALT_ID.getName())).thenReturn(reviewCount);

        Long getReviewCount = reviewService.getReviewCount(PRODUCT_ALT_ID.getName());
        assertEquals(reviewCount, getReviewCount);
    }
}
