package com.refactoringhabit.review.domain.service;

import com.refactoringhabit.review.domain.repository.ReviewRepository;
import com.refactoringhabit.review.dto.ReviewDetailDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private static final String ORDER_BY_VALUE = "createAt";

    @Transactional(readOnly = true)
    public List<ReviewDetailDto> getReviewDetailDtoList(String productAltId, Pageable pageable) {
        return reviewRepository.findByProductIdLimit(productAltId, pageable, ORDER_BY_VALUE);
    }

    @Transactional(readOnly = true)
    public List<ReviewDetailDto> getReviewDetailDtoList(String productAltId, Pageable pageable,
        String orderByValue) {
        return reviewRepository.findByProductIdLimit(productAltId, pageable, orderByValue);
    }

    @Transactional(readOnly = true)
    public Long getReviewCount(String productAltId) {
        return reviewRepository.countByProductId(productAltId);
    }
}
