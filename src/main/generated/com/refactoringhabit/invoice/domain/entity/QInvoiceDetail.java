package com.refactoringhabit.invoice.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInvoiceDetail is a Querydsl query type for InvoiceDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInvoiceDetail extends EntityPathBase<InvoiceDetail> {

    private static final long serialVersionUID = -440650225L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInvoiceDetail invoiceDetail = new QInvoiceDetail("invoiceDetail");

    public final com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity _super = new com.refactoringhabit.common.domain.entity.QBaseCreateTimeEntity(this);

    public final StringPath altId = createString("altId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QInvoice invoice;

    public final com.refactoringhabit.product.domain.entity.QProduct product;

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public final NumberPath<Integer> totalQuantity = createNumber("totalQuantity", Integer.class);

    public QInvoiceDetail(String variable) {
        this(InvoiceDetail.class, forVariable(variable), INITS);
    }

    public QInvoiceDetail(Path<? extends InvoiceDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInvoiceDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInvoiceDetail(PathMetadata metadata, PathInits inits) {
        this(InvoiceDetail.class, metadata, inits);
    }

    public QInvoiceDetail(Class<? extends InvoiceDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.invoice = inits.isInitialized("invoice") ? new QInvoice(forProperty("invoice"), inits.get("invoice")) : null;
        this.product = inits.isInitialized("product") ? new com.refactoringhabit.product.domain.entity.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

