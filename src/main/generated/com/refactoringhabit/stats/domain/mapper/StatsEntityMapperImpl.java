package com.refactoringhabit.stats.domain.mapper;

import com.refactoringhabit.order.dto.OrderSummaryByHostIdDto;
import com.refactoringhabit.order.dto.OrderSummaryByProductIdDto;
import com.refactoringhabit.review.dto.ReviewSummaryDto;
import com.refactoringhabit.stats.domain.entity.HostTotalSalesStats;
import com.refactoringhabit.stats.domain.entity.ProductTotalSalesStats;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-17T17:29:54+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class StatsEntityMapperImpl implements StatsEntityMapper {

    @Override
    public void updateProductTotalSalesStatsEntity(ProductTotalSalesStats productTotalSalesStats, OrderSummaryByProductIdDto orderSummaryDto, ReviewSummaryDto reviewSummaryDto, Long viewCount, Long wishCount) {
        if ( orderSummaryDto == null && reviewSummaryDto == null && viewCount == null && wishCount == null ) {
            return;
        }

        if ( orderSummaryDto != null ) {
            productTotalSalesStats.setSalesVolume( orderSummaryDto.salesVolume() );
            productTotalSalesStats.setSalesAmount( orderSummaryDto.salesAmount() );
            productTotalSalesStats.setMinPrice( orderSummaryDto.minPrice() );
            productTotalSalesStats.setMaxPrice( orderSummaryDto.maxPrice() );
        }
        if ( reviewSummaryDto != null ) {
            productTotalSalesStats.setReviewCount( reviewSummaryDto.reviewCount() );
            productTotalSalesStats.setReviewAverage( reviewSummaryDto.reviewAverage() );
        }
        productTotalSalesStats.setViewCount( viewCount );
        productTotalSalesStats.setWishCount( wishCount );
    }

    @Override
    public void updateHostTotalSalesStatsEntity(HostTotalSalesStats hostTotalSalesStats, OrderSummaryByHostIdDto orderSummaryDto, Long refundCount, ReviewSummaryDto reviewSummaryDto) {
        if ( orderSummaryDto == null && refundCount == null && reviewSummaryDto == null ) {
            return;
        }

        if ( orderSummaryDto != null ) {
            if ( orderSummaryDto.salesVolume() != null ) {
                hostTotalSalesStats.setSalesVolume( orderSummaryDto.salesVolume().intValue() );
            }
            if ( orderSummaryDto.salesAmount() != null ) {
                hostTotalSalesStats.setSalesAmount( orderSummaryDto.salesAmount().intValue() );
            }
        }
        if ( reviewSummaryDto != null ) {
            if ( reviewSummaryDto.reviewCount() != null ) {
                hostTotalSalesStats.setReviewCount( reviewSummaryDto.reviewCount().intValue() );
            }
            if ( reviewSummaryDto.reviewAverage() != null ) {
                hostTotalSalesStats.setReviewAverage( reviewSummaryDto.reviewAverage().intValue() );
            }
        }
        if ( refundCount != null ) {
            hostTotalSalesStats.setRefundCount( refundCount.intValue() );
        }
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
