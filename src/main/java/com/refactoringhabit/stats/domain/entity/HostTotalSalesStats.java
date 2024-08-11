package com.refactoringhabit.stats.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
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
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Table(name = "host_total_sales_stats")
@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HostTotalSalesStats extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "host_id")
    private Long hostId;

    @Column(name = "alt_id")
    private String altId;

    @Setter
    @Column(name = "total_product_count")
    private Long totalProductCount;

    @Setter
    @Column(name = "sales_volume")
    private Long salesVolume;

    @Setter
    @Column(name = "sales_amount")
    private Long salesAmount;

    @Setter
    @Column(name = "refund_count")
    private Long refundCount;

    @Setter
    @Column(name = "review_count")
    private Long reviewCount;

    @Setter
    @Column(name = "review_average")
    private Long reviewAverage;

    @Builder
    public HostTotalSalesStats(Long hostId, String altId) {
        this.hostId = hostId;
        this.altId = altId;
    }
}
