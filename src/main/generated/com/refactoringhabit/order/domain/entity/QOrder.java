package com.refactoringhabit.order.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -773589280L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final com.refactoringhabit.common.domain.entity.QBaseTimeEntity _super = new com.refactoringhabit.common.domain.entity.QBaseTimeEntity(this);

    public final StringPath altId = createString("altId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.refactoringhabit.host.domain.entity.QHost host;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.refactoringhabit.member.domain.entity.QMember member;

    public final com.refactoringhabit.product.domain.entity.QOption option;

    public final EnumPath<com.refactoringhabit.order.domain.enums.PayMethod> paymentMethod = createEnum("paymentMethod", com.refactoringhabit.order.domain.enums.PayMethod.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final EnumPath<com.refactoringhabit.order.domain.enums.RefundStatus> refundStatus = createEnum("refundStatus", com.refactoringhabit.order.domain.enums.RefundStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath usedAt = createString("usedAt");

    public final EnumPath<com.refactoringhabit.order.domain.enums.UsedStatus> usedStatus = createEnum("usedStatus", com.refactoringhabit.order.domain.enums.UsedStatus.class);

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.host = inits.isInitialized("host") ? new com.refactoringhabit.host.domain.entity.QHost(forProperty("host"), inits.get("host")) : null;
        this.member = inits.isInitialized("member") ? new com.refactoringhabit.member.domain.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.option = inits.isInitialized("option") ? new com.refactoringhabit.product.domain.entity.QOption(forProperty("option"), inits.get("option")) : null;
    }

}

