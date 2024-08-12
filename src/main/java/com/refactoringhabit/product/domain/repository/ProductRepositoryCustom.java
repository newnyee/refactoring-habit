package com.refactoringhabit.product.domain.repository;

import com.refactoringhabit.product.dto.ProductCardDto;
import com.refactoringhabit.product.dto.ProductDetailDto;
import com.refactoringhabit.product.dto.ProductSummaryDto;
import com.refactoringhabit.product.dto.SimpleProductInfoDto;
import java.util.List;
import org.springframework.data.domain.Pageable;


public interface ProductRepositoryCustom {
    ProductSummaryDto findMinAndMaxProductPrice(Long productId);
    List<ProductCardDto> productsOrderByCreatedAt();
    List<ProductCardDto> productsOrderBySalesVolumeAndReviewAverage();
    List<ProductCardDto> findByCategoryName(String categoryLargeEngName, Pageable pageable, String orderByValue);
    Long countByCategoryName(String categoryEngName);
    ProductDetailDto getProductDetailsByAltId(String altId);
    SimpleProductInfoDto getSimpleProductInfoByAltId(String altId);
}
