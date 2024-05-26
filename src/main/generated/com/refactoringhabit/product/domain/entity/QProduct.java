package com.refactoringhabit.product.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = -1368738270L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity _super = new com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity(this);

    public final StringPath address1 = createString("address1");

    public final StringPath address2 = createString("address2");

    public final StringPath altId = createString("altId");

    public final com.refactoringhabit.category.domain.entity.QCategory category;

    public final StringPath closedAt = createString("closedAt");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath extraAddress = createString("extraAddress");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final com.refactoringhabit.member.domain.entity.QMember member;

    public final StringPath name = createString("name");

    public final EnumPath<com.refactoringhabit.product.domain.enums.ProductStatus> status = createEnum("status", com.refactoringhabit.product.domain.enums.ProductStatus.class);

    public final StringPath tagAge = createString("tagAge");

    public final StringPath tagGender = createString("tagGender");

    public final StringPath tagWith = createString("tagWith");

    public final EnumPath<com.refactoringhabit.product.domain.enums.ProductType> type = createEnum("type", com.refactoringhabit.product.domain.enums.ProductType.class);

    public final NumberPath<Long> view = createNumber("view", Long.class);

    public final StringPath zipCode = createString("zipCode");

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.refactoringhabit.category.domain.entity.QCategory(forProperty("category")) : null;
        this.member = inits.isInitialized("member") ? new com.refactoringhabit.member.domain.entity.QMember(forProperty("member")) : null;
    }

}

