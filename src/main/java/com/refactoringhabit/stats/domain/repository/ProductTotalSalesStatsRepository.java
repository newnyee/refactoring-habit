package com.refactoringhabit.stats.domain.repository;

import com.refactoringhabit.stats.domain.entity.ProductTotalSalesStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTotalSalesStatsRepository
    extends JpaRepository<ProductTotalSalesStats, Long>, ProductTotalSalesStatsRepositoryCustom {

}
