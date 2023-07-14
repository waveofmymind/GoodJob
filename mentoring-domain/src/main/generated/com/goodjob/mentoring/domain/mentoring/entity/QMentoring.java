package com.goodjob.mentoring.domain.mentoring.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMentoring is a Querydsl query type for Mentoring
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMentoring extends EntityPathBase<Mentoring> {

    private static final long serialVersionUID = 226894885L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMentoring mentoring = new QMentoring("mentoring");

    public final com.goodjob.common.QBaseEntity _super = new com.goodjob.common.QBaseEntity(this);

    public final StringPath career = createString("career");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath currentJob = createString("currentJob");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath job = createString("job");

    public final com.goodjob.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath preferredTime = createString("preferredTime");

    public final StringPath title = createString("title");

    public QMentoring(String variable) {
        this(Mentoring.class, forVariable(variable), INITS);
    }

    public QMentoring(Path<? extends Mentoring> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMentoring(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMentoring(PathMetadata metadata, PathInits inits) {
        this(Mentoring.class, metadata, inits);
    }

    public QMentoring(Class<? extends Mentoring> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.goodjob.member.entity.QMember(forProperty("member")) : null;
    }

}

