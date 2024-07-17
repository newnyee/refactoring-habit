package com.refactoringhabit.review.domain.repository;

import com.refactoringhabit.review.dto.ReviewSummaryDto;

public interface ReviewRepositoryCustom {
    ReviewSummaryDto reviewSummaryByProductId(Long productId);
    ReviewSummaryDto reviewSummaryByHostId(Long hostId);
}
