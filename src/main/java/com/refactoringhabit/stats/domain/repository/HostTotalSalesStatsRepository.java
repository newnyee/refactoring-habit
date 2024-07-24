package com.refactoringhabit.stats.domain.repository;

import com.refactoringhabit.stats.domain.entity.HostTotalSalesStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostTotalSalesStatsRepository
    extends JpaRepository<HostTotalSalesStats, Long> {

}
