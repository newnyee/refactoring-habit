package com.refactoringhabit.category.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategoryLarge is a Querydsl query type for CategoryLarge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategoryLarge extends EntityPathBase<CategoryLarge> {

    private static final long serialVersionUID = -1719595303L;

    public static final QCategoryLarge categoryLarge = new QCategoryLarge("categoryLarge");

    public final com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity _super = new com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity(this);

    public final StringPath altId = createString("altId");

    public final ListPath<CategoryMiddle, QCategoryMiddle> categoryMiddles = this.<CategoryMiddle, QCategoryMiddle>createList("categoryMiddles", CategoryMiddle.class, QCategoryMiddle.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath engName = createString("engName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final EnumPath<com.refactoringhabit.category.domain.enums.CategoryStatus> status = createEnum("status", com.refactoringhabit.category.domain.enums.CategoryStatus.class);

    public QCategoryLarge(String variable) {
        super(CategoryLarge.class, forVariable(variable));
    }

    public QCategoryLarge(Path<? extends CategoryLarge> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategoryLarge(PathMetadata metadata) {
        super(CategoryLarge.class, metadata);
    }

}

