package com.refactoringhabit.review.domain.service;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.AttributeNames.PRODUCT_ALT_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.common.utils.CustomFileUtil;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.review.domain.entity.Review;
import com.refactoringhabit.review.domain.enums.ReviewStatus;
import com.refactoringhabit.review.domain.repository.ReviewRepository;
import com.refactoringhabit.review.dto.ReviewDetailDto;
import com.refactoringhabit.review.dto.ReviewUpdateRequestDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CustomFileUtil customFileUtil;

    @Mock
    private Member member;

    @Mock
    private Review review;

    @Mock
    private List<MultipartFile> multipartFiles;

    private static final String REVIEW_ALT_ID = "reviewAltId";

    @Test
    @DisplayName("상품 아이디에 따른 리뷰 목록 얻기 - two parameters")
    void testGetReviewDetailDtoListByProductId_TwoParameters() {
        List<ReviewDetailDto> reviewDetailDtoList = mock(List.class);
        Pageable pageable = mock(Pageable.class);
        String orderByValue = "createAt";
        when(reviewRepository
            .findByProductIdLimit(PRODUCT_ALT_ID.getName(), pageable, orderByValue))
            .thenReturn(reviewDetailDtoList);

        List<ReviewDetailDto> getReviewDetailDtoList = reviewService.getReviewDetailDtoListByProductId(
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

        List<ReviewDetailDto> getReviewDetailDtoList = reviewService.getReviewDetailDtoListByProductId(
            PRODUCT_ALT_ID.getName(), pageable, orderByValue);
        assertEquals(reviewDetailDtoList, getReviewDetailDtoList);
    }

    @Test
    @DisplayName("상품 아이디에 따른 리뷰 수 얻기")
    void testGetReviewCountByProductId() {
        Long reviewCount = 10L;
        when(reviewRepository.countByProductId(PRODUCT_ALT_ID.getName())).thenReturn(reviewCount);

        Long getReviewCount = reviewService.getReviewCountByProductId(PRODUCT_ALT_ID.getName());
        assertEquals(reviewCount, getReviewCount);
    }

    @Test
    @DisplayName("회원 아이디에 따른 리뷰 목록 얻기")
    void testGetReviewDetailDtoByMemberId() {
        Pageable pageable = mock(Pageable.class);
        List<ReviewDetailDto> reviewDetailDtoList = mock(List.class);
        when(reviewRepository
            .findByMemberIdLimit(MEMBER_ALT_ID.getName(), pageable))
            .thenReturn(reviewDetailDtoList);

        List<ReviewDetailDto> getReviewDetailDtoList =
            reviewService.getReviewDetailDtoListByMemberId(MEMBER_ALT_ID.getName(), pageable);
        assertEquals(reviewDetailDtoList, getReviewDetailDtoList);
    }

    @Test
    @DisplayName("회원 아이디에 따른 리뷰 수 얻기")
    void testGetReviewCountByMemberId() {
        Long reviewCount = 10L;
        when(reviewRepository.countByMemberId(MEMBER_ALT_ID.getName())).thenReturn(reviewCount);

        Long getReviewCount = reviewService.getReviewCountByMemberId(MEMBER_ALT_ID.getName());
        assertEquals(reviewCount, getReviewCount);
    }

    @Test
    @DisplayName("리뷰 아이디에 따른 리뷰 얻기")
    void testGetReview() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(reviewRepository.findByAltIdAndMember(REVIEW_ALT_ID, member)).thenReturn(review);

        reviewService.getReview(MEMBER_ALT_ID.getName(), REVIEW_ALT_ID);
        verify(review).getStarScore();
        verify(review).getContent();
        verify(review).getImage();
    }

    @Test
    @DisplayName("리뷰 삭제 - 성공")
    void testDeleteReview_Success() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(reviewRepository.findByAltIdAndMember(REVIEW_ALT_ID, member)).thenReturn(review);

        reviewService.deleteReview(MEMBER_ALT_ID.getName(), REVIEW_ALT_ID);
        verify(review).setStatus(ReviewStatus.HIDE);
    }

    @Test
    @DisplayName("리뷰 삭제 - 실패 : 찾을 수 없는 사용자")
    void testDeleteReview_NotFoundUser() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class,
            () -> reviewService.deleteReview(MEMBER_ALT_ID.getName(), REVIEW_ALT_ID));
    }

    @Test
    @DisplayName("리뷰 수정 - 성공")
    void testUpdateReview_Success() {
        ReviewUpdateRequestDto reviewUpdateRequestDto = mock(ReviewUpdateRequestDto.class);
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(reviewRepository.findByAltIdAndMember(REVIEW_ALT_ID, member)).thenReturn(review);
        when(customFileUtil.saveImageFiles(multipartFiles)).thenReturn("savedImageFiles");
        when(reviewUpdateRequestDto.getImage()).thenReturn("image");

        reviewService.updateReview(MEMBER_ALT_ID.getName(), REVIEW_ALT_ID, reviewUpdateRequestDto, multipartFiles);
    }

    @Test
    @DisplayName("리뷰 수정 - 성공 : 이미지 파일이 없는 경우")
    void testUpdateReview_Success_NoImageFile() {
        ReviewUpdateRequestDto reviewUpdateRequestDto = mock(ReviewUpdateRequestDto.class);
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(reviewRepository.findByAltIdAndMember(REVIEW_ALT_ID, member)).thenReturn(review);
        when(reviewUpdateRequestDto.getImage()).thenReturn("");

        reviewService.updateReview(MEMBER_ALT_ID.getName(), REVIEW_ALT_ID, reviewUpdateRequestDto, null);
    }
}
