package com.goodjob.core.domain.subComment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubComment is a Querydsl query type for SubComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubComment extends EntityPathBase<SubComment> {

    private static final long serialVersionUID = -999736815L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubComment subComment = new QSubComment("subComment");

    public final com.goodjob.core.domain.QBaseEntity _super = new com.goodjob.core.domain.QBaseEntity(this);

    public final com.goodjob.core.domain.comment.entity.QComment comment;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final ListPath<com.goodjob.core.domain.likes.entity.Likes, com.goodjob.core.domain.likes.entity.QLikes> likesList = this.<com.goodjob.core.domain.likes.entity.Likes, com.goodjob.core.domain.likes.entity.QLikes>createList("likesList", com.goodjob.core.domain.likes.entity.Likes.class, com.goodjob.core.domain.likes.entity.QLikes.class, PathInits.DIRECT2);

    public final com.goodjob.core.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public QSubComment(String variable) {
        this(SubComment.class, forVariable(variable), INITS);
    }

    public QSubComment(Path<? extends SubComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubComment(PathMetadata metadata, PathInits inits) {
        this(SubComment.class, metadata, inits);
    }

    public QSubComment(Class<? extends SubComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new com.goodjob.core.domain.comment.entity.QComment(forProperty("comment"), inits.get("comment")) : null;
        this.member = inits.isInitialized("member") ? new com.goodjob.core.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

