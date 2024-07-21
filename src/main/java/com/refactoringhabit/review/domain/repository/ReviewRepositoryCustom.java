package com.refactoringhabit.review.domain.repository;

import com.refactoringhabit.review.dto.ReviewSummaryDto;
import java.time.LocalDate;

public interface ReviewRepositoryCustom {
    ReviewSummaryDto reviewSummaryByProductId(Long productId);
    ReviewSummaryDto reviewSummaryByHostId(Long hostId);
    ReviewSummaryDto reviewSummaryByHostIdAndDate(Long hostId, LocalDate date);
}
