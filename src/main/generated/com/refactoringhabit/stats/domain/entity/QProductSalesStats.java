package com.refactoringhabit.stats.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductSalesStats is a Querydsl query type for ProductSalesStats
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductSalesStats extends EntityPathBase<ProductSalesStats> {

    private static final long serialVersionUID = 708721445L;

    public static final QProductSalesStats productSalesStats = new QProductSalesStats("productSalesStats");

    public final StringPath altId = createString("altId");

    public final NumberPath<Long> categoryMiddleId = createNumber("categoryMiddleId", Long.class);

    public final NumberPath<Long> hostId = createNumber("hostId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> maxPrice = createNumber("maxPrice", Integer.class);

    public final NumberPath<Integer> minPrice = createNumber("minPrice", Integer.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final NumberPath<Double> reviewAverage = createNumber("reviewAverage", Double.class);

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final NumberPath<Integer> salesAmount = createNumber("salesAmount", Integer.class);

    public final NumberPath<Integer> salesVolume = createNumber("salesVolume", Integer.class);

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QProductSalesStats(String variable) {
        super(ProductSalesStats.class, forVariable(variable));
    }

    public QProductSalesStats(Path<? extends ProductSalesStats> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductSalesStats(PathMetadata metadata) {
        super(ProductSalesStats.class, metadata);
    }

}

