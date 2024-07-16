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

    public final NumberPath<Integer> refundCount = createNumber("refundCount", Integer.class);

    public final NumberPath<Integer> reviewAverage = createNumber("reviewAverage", Integer.class);

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final NumberPath<Integer> salesAmount = createNumber("salesAmount", Integer.class);

    public final NumberPath<Integer> salesVolume = createNumber("salesVolume", Integer.class);

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

