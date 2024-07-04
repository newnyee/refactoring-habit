package com.refactoringhabit.stats.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHostSalesStats is a Querydsl query type for HostSalesStats
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHostSalesStats extends EntityPathBase<HostSalesStats> {

    private static final long serialVersionUID = -897951816L;

    public static final QHostSalesStats hostSalesStats = new QHostSalesStats("hostSalesStats");

    public final StringPath altId = createString("altId");

    public final NumberPath<Long> hostId = createNumber("hostId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> refundCount = createNumber("refundCount", Integer.class);

    public final NumberPath<Integer> reviewAverage = createNumber("reviewAverage", Integer.class);

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final NumberPath<Integer> salesAmount = createNumber("salesAmount", Integer.class);

    public final NumberPath<Integer> salesVolume = createNumber("salesVolume", Integer.class);

    public QHostSalesStats(String variable) {
        super(HostSalesStats.class, forVariable(variable));
    }

    public QHostSalesStats(Path<? extends HostSalesStats> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHostSalesStats(PathMetadata metadata) {
        super(HostSalesStats.class, metadata);
    }

}

