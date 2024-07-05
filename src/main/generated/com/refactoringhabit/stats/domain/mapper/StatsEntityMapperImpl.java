package com.refactoringhabit.stats.domain.mapper;

import com.refactoringhabit.stats.domain.entity.HostTotalSalesStats;
import com.refactoringhabit.stats.domain.entity.ProductTotalSalesStats;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-05T14:05:29+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class StatsEntityMapperImpl implements StatsEntityMapper {

    @Override
    public ProductTotalSalesStats toProductSalesStatsEntity(Long hostId, Long productId, Long categoryMiddleId, String altId) {
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
