package com.refactoringhabit.stats.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHostMonthlySalesStats is a Querydsl query type for HostMonthlySalesStats
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHostMonthlySalesStats extends EntityPathBase<HostMonthlySalesStats> {

    private static final long serialVersionUID = -958671237L;

    public static final QHostMonthlySalesStats hostMonthlySalesStats = new QHostMonthlySalesStats("hostMonthlySalesStats");

    public final com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity _super = new com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity(this);

    public final StringPath altId = createString("altId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> hostId = createNumber("hostId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> refundCount = createNumber("refundCount", Integer.class);

    public final NumberPath<Integer> salesAmount = createNumber("salesAmount", Integer.class);

    public final NumberPath<Integer> salesVolume = createNumber("salesVolume", Integer.class);

    public QHostMonthlySalesStats(String variable) {
        super(HostMonthlySalesStats.class, forVariable(variable));
    }

    public QHostMonthlySalesStats(Path<? extends HostMonthlySalesStats> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHostMonthlySalesStats(PathMetadata metadata) {
        super(HostMonthlySalesStats.class, metadata);
    }

}

