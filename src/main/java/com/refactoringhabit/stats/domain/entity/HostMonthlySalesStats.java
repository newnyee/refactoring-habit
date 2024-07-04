package com.refactoringhabit.stats.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseCreateTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Table(name = "host_monthly_sales_stats")
@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HostMonthlySalesStats extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "host_id")
    private Long hostId;

    @Column(name = "alt_id")
    private String altId;

    @Column(name = "sales_volume")
    private int salesVolume;

    @Column(name = "sales_amount")
    private int salesAmount;

    @Column(name = "refund_count")
    private int refundCount;

    @Builder
    public HostMonthlySalesStats(Long hostId, String altId) {
        this.hostId = hostId;
        this.altId = altId;
    }
}
