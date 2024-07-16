package com.refactoringhabit.product.domain.repository;

import com.refactoringhabit.home.dto.HomeProductListDto;
import java.util.List;

public interface ProductRepositoryCustom {
    List<HomeProductListDto> productsOrderByCreatedAt();
}
