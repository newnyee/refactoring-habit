package com.refactoringhabit.product.domain.repository;

import com.refactoringhabit.product.dto.HomeProductDto;
import com.refactoringhabit.product.dto.ProductSummaryDto;
import java.util.List;

public interface ProductRepositoryCustom {
    ProductSummaryDto findMinAndMaxProductPrice(Long productId);
    List<HomeProductDto> productsOrderByCreatedAt();
    List<HomeProductDto> productsOrderBySalesVolumeAndReviewAverage();
}
