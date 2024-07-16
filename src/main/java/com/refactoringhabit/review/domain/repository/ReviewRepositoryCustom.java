package com.refactoringhabit.review.domain.repository;

import com.refactoringhabit.review.dto.ReviewSummaryByProductIdDto;

public interface ReviewRepositoryCustom {
    ReviewSummaryByProductIdDto reviewSummaryByProductId(Long productId);
}
