package com.refactoringhabit.review.domain.repository;

import com.refactoringhabit.review.dto.ReviewDetailDto;
import com.refactoringhabit.review.dto.ReviewSummaryDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    ReviewSummaryDto reviewSummaryByProductId(Long productId);
    ReviewSummaryDto reviewSummaryByHostId(Long hostId);
    ReviewSummaryDto reviewSummaryByHostIdAndDate(Long hostId, LocalDate date);
    List<ReviewDetailDto> findByProductIdLimit(String productAltId, Pageable pageable);
}
