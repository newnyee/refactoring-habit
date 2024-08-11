package com.refactoringhabit.stats.domain.mapper;

import com.refactoringhabit.order.dto.OrderSummaryDto;
import com.refactoringhabit.product.dto.ProductSummaryDto;
import com.refactoringhabit.review.dto.ReviewSummaryDto;
import com.refactoringhabit.stats.domain.entity.HostDailySalesStats;
import com.refactoringhabit.stats.domain.entity.HostTotalSalesStats;
import com.refactoringhabit.stats.domain.entity.ProductTotalSalesStats;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-10T07:48:27+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class StatsEntityMapperImpl implements StatsEntityMapper {

    @Override
    public void updateProductTotalSalesStatsEntity(ProductTotalSalesStats productTotalSalesStats, OrderSummaryDto orderSummaryDto, ProductSummaryDto productSummaryDto, ReviewSummaryDto reviewSummaryDto, Long viewCount, Long wishCount) {
        if ( orderSummaryDto == null && productSummaryDto == null && reviewSummaryDto == null && viewCount == null && wishCount == null ) {
            return;
        }

        if ( orderSummaryDto != null ) {
            productTotalSalesStats.setSalesVolume( orderSummaryDto.salesVolume() );
            productTotalSalesStats.setSalesAmount( orderSummaryDto.salesAmount() );
        }
        if ( productSummaryDto != null ) {
            productTotalSalesStats.setMinPrice( productSummaryDto.minPrice() );
            productTotalSalesStats.setMaxPrice( productSummaryDto.maxPrice() );
        }
        if ( reviewSummaryDto != null ) {
            productTotalSalesStats.setReviewCount( reviewSummaryDto.reviewCount() );
            productTotalSalesStats.setReviewAverage( reviewSummaryDto.reviewAverage() );
        }
        productTotalSalesStats.setViewCount( viewCount );
        productTotalSalesStats.setWishCount( wishCount );
    }

    @Override
    public void updateHostTotalSalesStatsEntity(HostTotalSalesStats hostTotalSalesStats, Long totalProductCount, OrderSummaryDto orderSummaryDto, Long refundCount, ReviewSummaryDto reviewSummaryDto) {
        if ( totalProductCount == null && orderSummaryDto == null && refundCount == null && reviewSummaryDto == null ) {
            return;
        }

        if ( orderSummaryDto != null ) {
            hostTotalSalesStats.setSalesVolume( orderSummaryDto.salesVolume() );
            hostTotalSalesStats.setSalesAmount( orderSummaryDto.salesAmount() );
        }
        if ( reviewSummaryDto != null ) {
            hostTotalSalesStats.setReviewCount( reviewSummaryDto.reviewCount() );
            if ( reviewSummaryDto.reviewAverage() != null ) {
                hostTotalSalesStats.setReviewAverage( reviewSummaryDto.reviewAverage().longValue() );
            }
            else {
                hostTotalSalesStats.setReviewAverage( null );
            }
        }
        hostTotalSalesStats.setTotalProductCount( totalProductCount );
        hostTotalSalesStats.setRefundCount( refundCount );
    }

    @Override
    public HostDailySalesStats toHostDailySalesStatsEntity(Long hostId, OrderSummaryDto orderSummaryDto, Long refundCount, ReviewSummaryDto reviewSummaryDto, String altId) {
        if ( hostId == null && orderSummaryDto == null && refundCount == null && reviewSummaryDto == null && altId == null ) {
            return null;
        }

        HostDailySalesStats.HostDailySalesStatsBuilder hostDailySalesStats = HostDailySalesStats.builder();

        if ( orderSummaryDto != null ) {
            hostDailySalesStats.salesVolume( orderSummaryDto.salesVolume() );
            hostDailySalesStats.salesAmount( orderSummaryDto.salesAmount() );
        }
        if ( reviewSummaryDto != null ) {
            hostDailySalesStats.reviewCount( reviewSummaryDto.reviewCount() );
            hostDailySalesStats.reviewAverage( reviewSummaryDto.reviewAverage() );
        }
        hostDailySalesStats.hostId( hostId );
        hostDailySalesStats.refundCount( refundCount );
        hostDailySalesStats.altId( generateUuid( altId ) );

        return hostDailySalesStats.build();
    }

    @Override
    public ProductTotalSalesStats toProductTotalSalesStatsEntity(Long hostId, Long productId, Long categoryMiddleId, String altId) {
        if ( hostId == null && productId == null && categoryMiddleId == null && altId == null ) {
            return null;
        }

        ProductTotalSalesStats.ProductTotalSalesStatsBuilder productTotalSalesStats = ProductTotalSalesStats.builder();

        productTotalSalesStats.hostId( hostId );
        productTotalSalesStats.productId( productId );
        productTotalSalesStats.categoryMiddleId( categoryMiddleId );
        productTotalSalesStats.altId( generateUuid( altId ) );

        return productTotalSalesStats.build();
    }

    @Override
    public HostTotalSalesStats toHostSalesStatsEntity(Long hostId, String altId) {
        if ( hostId == null && altId == null ) {
            return null;
        }

        HostTotalSalesStats.HostTotalSalesStatsBuilder hostTotalSalesStats = HostTotalSalesStats.builder();

        hostTotalSalesStats.hostId( hostId );
        hostTotalSalesStats.altId( generateUuid( altId ) );

        return hostTotalSalesStats.build();
    }
}
