package com.refactoringhabit.product.domain.repository;

import com.refactoringhabit.product.dto.ProductCardDto;
import com.refactoringhabit.product.dto.ProductSummaryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;


public interface ProductRepositoryCustom {
    ProductSummaryDto findMinAndMaxProductPrice(Long productId);
    List<ProductCardDto> productsOrderByCreatedAt();
    List<ProductCardDto> productsOrderBySalesVolumeAndReviewAverage();
    List<ProductCardDto> findByCategoryName(String categoryLargeEngName, Pageable pageable);
    Long countByCategoryName(String categoryEngName);
}
