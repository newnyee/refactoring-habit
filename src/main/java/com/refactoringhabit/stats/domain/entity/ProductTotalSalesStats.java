package com.refactoringhabit.stats.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
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
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Table(name = "product_total_sales_stats")
@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductTotalSalesStats extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "host_id")
    private Long hostId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "category_middle_id")
    private Long categoryMiddleId;

    @Column(name = "alt_id")
    private String altId;

    @Setter
    @Column(name = "sales_volume")
    private Long salesVolume; // 판매량

    @Setter
    @Column(name = "sales_amount")
    private Long salesAmount;

    @Setter
    @Column(name = "view_count")
    private Long viewCount;

    @Setter
    @Column(name = "review_count")
    private Long reviewCount;

    @Setter
    @Column(name = "review_average")
    private BigDecimal reviewAverage;

    @Setter
    @Column(name = "wish_count")
    private Long wishCount;

    @Setter
    @Column(name = "min_price")
    private int minPrice;

    @Setter
    @Column(name = "max_price")
    private int maxPrice;

    @Builder
    public ProductTotalSalesStats(Long hostId, Long productId, Long categoryMiddleId, String altId) {
        this.hostId = hostId;
        this.productId = productId;
        this.categoryMiddleId = categoryMiddleId;
        this.altId = altId;
    }
}
