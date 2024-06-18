package com.refactoringhabit.category.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategoryMiddle is a Querydsl query type for CategoryMiddle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategoryMiddle extends EntityPathBase<CategoryMiddle> {

    private static final long serialVersionUID = -1732249161L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCategoryMiddle categoryMiddle = new QCategoryMiddle("categoryMiddle");

    public final com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity _super = new com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity(this);

    public final StringPath altId = createString("altId");

    public final QCategoryLarge categoryLarge;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final EnumPath<com.refactoringhabit.category.domain.enums.CategoryStatus> status = createEnum("status", com.refactoringhabit.category.domain.enums.CategoryStatus.class);

    public QCategoryMiddle(String variable) {
        this(CategoryMiddle.class, forVariable(variable), INITS);
    }

    public QCategoryMiddle(Path<? extends CategoryMiddle> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCategoryMiddle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCategoryMiddle(PathMetadata metadata, PathInits inits) {
        this(CategoryMiddle.class, metadata, inits);
    }

    public QCategoryMiddle(Class<? extends CategoryMiddle> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.categoryLarge = inits.isInitialized("categoryLarge") ? new QCategoryLarge(forProperty("categoryLarge")) : null;
    }

}

