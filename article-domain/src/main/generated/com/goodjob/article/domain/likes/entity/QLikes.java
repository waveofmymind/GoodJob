package com.goodjob.article.domain.likes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLikes is a Querydsl query type for Likes
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikes extends EntityPathBase<Likes> {

    private static final long serialVersionUID = 1604575354L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLikes likes = new QLikes("likes");

    public final com.goodjob.common.QBaseEntity _super = new com.goodjob.common.QBaseEntity(this);

    public final com.goodjob.article.domain.article.entity.QArticle article;

    public final com.goodjob.article.domain.comment.entity.QComment comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.goodjob.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final com.goodjob.article.domain.subComment.entity.QSubComment subComment;

    public QLikes(String variable) {
        this(Likes.class, forVariable(variable), INITS);
    }

    public QLikes(Path<? extends Likes> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLikes(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLikes(PathMetadata metadata, PathInits inits) {
        this(Likes.class, metadata, inits);
    }

    public QLikes(Class<? extends Likes> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new com.goodjob.article.domain.article.entity.QArticle(forProperty("article"), inits.get("article")) : null;
        this.comment = inits.isInitialized("comment") ? new com.goodjob.article.domain.comment.entity.QComment(forProperty("comment"), inits.get("comment")) : null;
        this.member = inits.isInitialized("member") ? new com.goodjob.member.entity.QMember(forProperty("member")) : null;
        this.subComment = inits.isInitialized("subComment") ? new com.goodjob.article.domain.subComment.entity.QSubComment(forProperty("subComment"), inits.get("subComment")) : null;
    }

}

