package com.refactoringhabit.stats.domain.mapper;

import com.refactoringhabit.order.dto.OrderSummaryByHostIdDto;
import com.refactoringhabit.order.dto.OrderSummaryByProductIdDto;
import com.refactoringhabit.review.dto.ReviewSummaryDto;
import com.refactoringhabit.stats.domain.entity.HostDailySalesStats;
import com.refactoringhabit.stats.domain.entity.HostTotalSalesStats;
import com.refactoringhabit.stats.domain.entity.ProductTotalSalesStats;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatsEntityMapper {

    StatsEntityMapper INSTANCE = Mappers.getMapper(StatsEntityMapper.class);

    void updateProductTotalSalesStatsEntity(
        @MappingTarget ProductTotalSalesStats productTotalSalesStats,
        OrderSummaryByProductIdDto orderSummaryDto, ReviewSummaryDto reviewSummaryDto,
        Long viewCount, Long wishCount);

    void updateHostTotalSalesStatsEntity(
        @MappingTarget HostTotalSalesStats hostTotalSalesStats,
        OrderSummaryByHostIdDto orderSummaryDto, Long refundCount,
        ReviewSummaryDto reviewSummaryDto
    );

    @Mapping(target = "altId", source = "altId", qualifiedByName = "generateUuid")
    HostDailySalesStats toHostDailySalesStatsEntity(
        Long hostId, OrderSummaryByHostIdDto orderSummaryDto, Long refundCount,
        ReviewSummaryDto reviewSummaryDto, String altId);

    @Mapping(target = "altId", source = "altId", qualifiedByName = "generateUuid")
    ProductTotalSalesStats toProductTotalSalesStatsEntity(Long hostId, Long productId,
        Long categoryMiddleId, String altId);

    @Mapping(target = "altId", source = "altId", qualifiedByName = "generateUuid")
    HostTotalSalesStats toHostSalesStatsEntity(Long hostId, String altId);

    @Named("generateUuid")
    default String generateUuid(String value) {
        return (value == null) ? UUID.randomUUID().toString() : value;
    }
}
