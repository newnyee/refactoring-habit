package com.refactoringhabit.stats.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductTotalSalesStats is a Querydsl query type for ProductTotalSalesStats
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductTotalSalesStats extends EntityPathBase<ProductTotalSalesStats> {

    private static final long serialVersionUID = -779998107L;

    public static final QProductTotalSalesStats productTotalSalesStats = new QProductTotalSalesStats("productTotalSalesStats");

    public final com.refactoringhabit.common.domain.entity.QBaseTimeEntity _super = new com.refactoringhabit.common.domain.entity.QBaseTimeEntity(this);

    public final StringPath altId = createString("altId");

    public final NumberPath<Long> categoryMiddleId = createNumber("categoryMiddleId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> hostId = createNumber("hostId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> maxPrice = createNumber("maxPrice", Integer.class);

    public final NumberPath<Integer> minPrice = createNumber("minPrice", Integer.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final NumberPath<java.math.BigDecimal> reviewAverage = createNumber("reviewAverage", java.math.BigDecimal.class);

    public final NumberPath<Long> reviewCount = createNumber("reviewCount", Long.class);

    public final NumberPath<Long> salesAmount = createNumber("salesAmount", Long.class);

    public final NumberPath<Long> salesVolume = createNumber("salesVolume", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public final NumberPath<Long> wishCount = createNumber("wishCount", Long.class);

    public QProductTotalSalesStats(String variable) {
        super(ProductTotalSalesStats.class, forVariable(variable));
    }

    public QProductTotalSalesStats(Path<? extends ProductTotalSalesStats> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductTotalSalesStats(PathMetadata metadata) {
        super(ProductTotalSalesStats.class, metadata);
    }

}

