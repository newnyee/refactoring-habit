package com.refactoringhabit.stats.domain.entity;

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

@Table(name = "product_sales_stats")
@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductSalesStats {

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

    @Column(name = "sales_volume")
    private int salesVolume;

    @Column(name = "sales_amount")
    private int salesAmount;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "review_count")
    private int reviewCount;

    @Column(name = "review_average")
    private double reviewAverage;

    @Column(name = "min_price")
    private int minPrice;

    @Column(name = "max_price")
    private int maxPrice;

    @Builder
    public ProductSalesStats(Long hostId, Long productId, Long categoryMiddleId, String altId) {
        this.hostId = hostId;
        this.productId = productId;
        this.categoryMiddleId = categoryMiddleId;
        this.altId = altId;
    }
}
