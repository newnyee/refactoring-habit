package com.refactoringhabit.review.domain.service;

import com.refactoringhabit.common.utils.CustomFileUtil;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.product.dto.SimpleProductInfoDto;
import com.refactoringhabit.review.domain.entity.Review;
import com.refactoringhabit.review.domain.enums.ReviewStatus;
import com.refactoringhabit.review.domain.mapper.ReviewEntityMapper;
import com.refactoringhabit.review.domain.repository.ReviewRepository;
import com.refactoringhabit.review.dto.ReviewDetailDto;
import com.refactoringhabit.review.dto.ReviewDetailListByProductResponseDto;
import com.refactoringhabit.review.dto.ReviewUpdateRequestDto;
import com.refactoringhabit.review.dto.ReviewUpdateResponseDto;
import java.util.List;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CustomFileUtil customFileUtil;

    @Transactional(readOnly = true)
    public ReviewDetailListByProductResponseDto getReviewDetailDtoListByProductId(
        String productAltId, Pageable pageable, String orderByValue) {
        return ReviewEntityMapper.INSTANCE.toReviewDetailListByProductResponseDto(
            getSimpleProductInfoByAltId(productAltId),
            getReviewCount(productAltId),
            getReviewDetails(productAltId, pageable, orderByValue));
    }

    @Transactional(readOnly = true)
    public List<ReviewDetailDto> getReviewDetailDtoListByMemberId(
        String memberAltId, Pageable pageable) {
        return reviewRepository.findByMemberIdLimit(memberAltId, pageable);
    }

    @Transactional(readOnly = true)
    public Long getReviewCountByMemberId(String memberAltId) {
        return reviewRepository.countByMemberId(memberAltId);
    }

    @Transactional(readOnly = true)
    public ReviewUpdateResponseDto getReview(String memberAltId, String reviewAltId) {
        return ReviewEntityMapper.INSTANCE
            .toReviewUpdateDto(getReview(reviewAltId, getMember(memberAltId)));
    }

    @Transactional
    public void deleteReview(String memberAltId, String reviewAltId) {
        Review review = getReview(reviewAltId, getMember(memberAltId));
        review.setStatus(ReviewStatus.HIDE);
    }

    @Transactional
    public void updateReview(String memberAltId, String reviewAltId,
        ReviewUpdateRequestDto reviewUpdateRequestDto, List<MultipartFile> multipartFiles) {
        ReviewEntityMapper.INSTANCE.updateEntity(
            getReview(reviewAltId, getMember(memberAltId)),
            reviewUpdateRequestDto,
            getImageFileNames(reviewUpdateRequestDto, multipartFiles));
    }

    private Member getMember(String memberAltId) {
        return memberRepository.findByAltId(memberAltId)
            .orElseThrow(UserNotFoundException::new);
    }

    private Review getReview(String reviewAltId, Member member) {
        return reviewRepository.findByAltIdAndMember(reviewAltId, member);
    }

    private String getImageFileNames(ReviewUpdateRequestDto reviewUpdateRequestDto,
        List<MultipartFile> multipartFiles) {
        String savedImageFiles = null;
        if (multipartFiles != null) {
            savedImageFiles = customFileUtil.saveImageFiles(multipartFiles);
        }

        StringJoiner joiner = new StringJoiner("|");
        if (!reviewUpdateRequestDto.getImage().isEmpty()) {
            joiner.add(reviewUpdateRequestDto.getImage());
        }
        if (savedImageFiles != null) {
            joiner.add(savedImageFiles);
        }
        return joiner.toString();
    }

    private SimpleProductInfoDto getSimpleProductInfoByAltId(String productAltId) {
        return productRepository.getSimpleProductInfoByAltId(productAltId);
    }

    private Long getReviewCount(String productAltId) {
        return reviewRepository.countByProductId(productAltId);
    }

    private List<ReviewDetailDto> getReviewDetails(String productAltId, Pageable pageable,
        String orderByValue) {
        return reviewRepository.findByProductIdLimit(productAltId,
            pageable, orderByValue);
    }
}
