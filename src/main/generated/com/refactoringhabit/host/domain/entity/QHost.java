package com.refactoringhabit.host.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHost is a Querydsl query type for Host
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHost extends EntityPathBase<Host> {

    private static final long serialVersionUID = 1445547202L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHost host = new QHost("host");

    public final com.refactoringhabit.common.domain.entity.QBaseTimeEntity _super = new com.refactoringhabit.common.domain.entity.QBaseTimeEntity(this);

    public final StringPath accountHolder = createString("accountHolder");

    public final StringPath accountNumber = createString("accountNumber");

    public final StringPath altId = createString("altId");

    public final StringPath bank = createString("bank");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduction = createString("introduction");

    public final com.refactoringhabit.member.domain.entity.QMember member;

    public final StringPath nickName = createString("nickName");

    public final StringPath phone = createString("phone");

    public final StringPath profileImage = createString("profileImage");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QHost(String variable) {
        this(Host.class, forVariable(variable), INITS);
    }

    public QHost(Path<? extends Host> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHost(PathMetadata metadata, PathInits inits) {
        this(Host.class, metadata, inits);
    }

    public QHost(Class<? extends Host> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.refactoringhabit.member.domain.entity.QMember(forProperty("member")) : null;
    }

}

