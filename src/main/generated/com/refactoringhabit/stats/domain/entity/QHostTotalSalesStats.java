package com.refactoringhabit.stats.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHostTotalSalesStats is a Querydsl query type for HostTotalSalesStats
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHostTotalSalesStats extends EntityPathBase<HostTotalSalesStats> {

    private static final long serialVersionUID = -1185942606L;

    public static final QHostTotalSalesStats hostTotalSalesStats = new QHostTotalSalesStats("hostTotalSalesStats");

    public final com.refactoringhabit.common.domain.entity.QBaseTimeEntity _super = new com.refactoringhabit.common.domain.entity.QBaseTimeEntity(this);

    public final StringPath altId = createString("altId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> hostId = createNumber("hostId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> refundCount = createNumber("refundCount", Long.class);

    public final NumberPath<Long> reviewAverage = createNumber("reviewAverage", Long.class);

    public final NumberPath<Long> reviewCount = createNumber("reviewCount", Long.class);

    public final NumberPath<Long> salesAmount = createNumber("salesAmount", Long.class);

    public final NumberPath<Long> salesVolume = createNumber("salesVolume", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QHostTotalSalesStats(String variable) {
        super(HostTotalSalesStats.class, forVariable(variable));
    }

    public QHostTotalSalesStats(Path<? extends HostTotalSalesStats> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHostTotalSalesStats(PathMetadata metadata) {
        super(HostTotalSalesStats.class, metadata);
    }

}

