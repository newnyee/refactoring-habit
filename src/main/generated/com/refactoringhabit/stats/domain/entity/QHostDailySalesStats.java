package com.refactoringhabit.stats.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHostDailySalesStats is a Querydsl query type for HostDailySalesStats
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHostDailySalesStats extends EntityPathBase<HostDailySalesStats> {

    private static final long serialVersionUID = 1712730183L;

    public static final QHostDailySalesStats hostDailySalesStats = new QHostDailySalesStats("hostDailySalesStats");

    public final com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity _super = new com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity(this);

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

    public QHostDailySalesStats(String variable) {
        super(HostDailySalesStats.class, forVariable(variable));
    }

    public QHostDailySalesStats(Path<? extends HostDailySalesStats> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHostDailySalesStats(PathMetadata metadata) {
        super(HostDailySalesStats.class, metadata);
    }

}

