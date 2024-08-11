package com.refactoringhabit.host.domain.repository;

import static com.refactoringhabit.host.domain.entity.QHost.host;
import static com.refactoringhabit.stats.domain.entity.QHostTotalSalesStats.hostTotalSalesStats;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.host.dto.SimpleHostInfoDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HostRepositoryImpl implements HostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SimpleHostInfoDto getSimpleHostInfoById(Long id) {
        return jpaQueryFactory
            .select(Projections.constructor(SimpleHostInfoDto.class,
                host.profileImage,
                host.nickName,
                hostTotalSalesStats.reviewCount,
                hostTotalSalesStats.totalProductCount))
            .from(host)
            .join(hostTotalSalesStats).on(host.id.eq(hostTotalSalesStats.hostId))
            .where(host.id.eq(id))
            .fetchOne();
    }
}
