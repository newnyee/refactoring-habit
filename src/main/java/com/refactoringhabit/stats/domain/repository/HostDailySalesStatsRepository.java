package com.refactoringhabit.stats.domain.repository;

import com.refactoringhabit.stats.domain.entity.HostDailySalesStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostDailySalesStatsRepository
    extends JpaRepository<HostDailySalesStats, Long> {

}
