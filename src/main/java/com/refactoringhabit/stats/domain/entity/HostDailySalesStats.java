package com.refactoringhabit.stats.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseCreateTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "host_daily_sales_stats")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HostDailySalesStats extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "host_id")
    private Long hostId;

    @Column(name = "alt_id")
    private String altId;

    @Column(name = "sales_volume")
    private Long salesVolume;

    @Column(name = "sales_amount")
    private Long salesAmount;

    @Column(name = "refund_count")
    private Long refundCount;

    @Column(name = "review_count")
    private Long reviewCount;

    @Column(name = "review_average")
    private BigDecimal reviewAverage;

    @Builder
    public HostDailySalesStats(Long hostId, String altId, Long salesVolume, Long salesAmount,
        Long refundCount, Long reviewCount, BigDecimal reviewAverage) {
        this.hostId = hostId;
        this.altId = altId;
        this.salesVolume = salesVolume;
        this.salesAmount = salesAmount;
        this.refundCount = refundCount;
        this.reviewCount = reviewCount;
        this.reviewAverage = reviewAverage;
    }
}
