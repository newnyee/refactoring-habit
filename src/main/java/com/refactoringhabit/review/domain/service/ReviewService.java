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

    @Transactional(readOnly = true)
    public List<ReviewDetailDto> getReviewDetailDtoList(String productAltId, Pageable pageable) {
        return reviewRepository.findByProductIdLimit(productAltId, pageable);
    }
}
