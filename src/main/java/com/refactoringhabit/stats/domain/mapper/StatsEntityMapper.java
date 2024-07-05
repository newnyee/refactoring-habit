package com.refactoringhabit.stats.domain.mapper;

import com.refactoringhabit.stats.domain.entity.HostTotalSalesStats;
import com.refactoringhabit.stats.domain.entity.ProductTotalSalesStats;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatsEntityMapper {

    StatsEntityMapper INSTANCE = Mappers.getMapper(StatsEntityMapper.class);

    @Mapping(target = "altId", source = "altId", qualifiedByName = "generateUuid")
    ProductTotalSalesStats toProductSalesStatsEntity(Long hostId, Long productId, Long categoryMiddleId, String altId);

    @Mapping(target = "altId", source = "altId", qualifiedByName = "generateUuid")
    HostTotalSalesStats toHostSalesStatsEntity(Long hostId, String altId);

    @Named("generateUuid")
    default String generateUuid(String value) {
        return (value == null) ? UUID.randomUUID().toString() : value;
    }
}
