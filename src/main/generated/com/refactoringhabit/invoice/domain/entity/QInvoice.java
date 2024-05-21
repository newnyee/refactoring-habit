package com.refactoringhabit.invoice.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInvoice is a Querydsl query type for Invoice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInvoice extends EntityPathBase<Invoice> {

    private static final long serialVersionUID = 1805795358L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInvoice invoice = new QInvoice("invoice");

    public final com.refactoringhabit.common.domain.entity.QBaseTimeEntity _super = new com.refactoringhabit.common.domain.entity.QBaseTimeEntity(this);

    public final StringPath accountHolder = createString("accountHolder");

    public final StringPath accountNumber = createString("accountNumber");

    public final StringPath altId = createString("altId");

    public final StringPath bank = createString("bank");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.refactoringhabit.host.domain.entity.QHost host;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> paymentAmount = createNumber("paymentAmount", Integer.class);

    public final StringPath period = createString("period");

    public final EnumPath<com.refactoringhabit.invoice.domain.enums.InvoiceStatus> status = createEnum("status", com.refactoringhabit.invoice.domain.enums.InvoiceStatus.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QInvoice(String variable) {
        this(Invoice.class, forVariable(variable), INITS);
    }

    public QInvoice(Path<? extends Invoice> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInvoice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInvoice(PathMetadata metadata, PathInits inits) {
        this(Invoice.class, metadata, inits);
    }

    public QInvoice(Class<? extends Invoice> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.host = inits.isInitialized("host") ? new com.refactoringhabit.host.domain.entity.QHost(forProperty("host"), inits.get("host")) : null;
    }

}

