package com.goodjob.article.domain.comment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QComment is a Querydsl query type for Comment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QComment extends EntityPathBase<Comment> {

    private static final long serialVersionUID = 8686810L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComment comment = new QComment("comment");

    public final com.goodjob.common.QBaseEntity _super = new com.goodjob.common.QBaseEntity(this);

    public final com.goodjob.article.domain.article.entity.QArticle article;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final ListPath<com.goodjob.article.domain.likes.entity.Likes, com.goodjob.article.domain.likes.entity.QLikes> likesList = this.<com.goodjob.article.domain.likes.entity.Likes, com.goodjob.article.domain.likes.entity.QLikes>createList("likesList", com.goodjob.article.domain.likes.entity.Likes.class, com.goodjob.article.domain.likes.entity.QLikes.class, PathInits.DIRECT2);

    public final com.goodjob.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final ListPath<com.goodjob.article.domain.subComment.entity.SubComment, com.goodjob.article.domain.subComment.entity.QSubComment> subCommentList = this.<com.goodjob.article.domain.subComment.entity.SubComment, com.goodjob.article.domain.subComment.entity.QSubComment>createList("subCommentList", com.goodjob.article.domain.subComment.entity.SubComment.class, com.goodjob.article.domain.subComment.entity.QSubComment.class, PathInits.DIRECT2);

    public QComment(String variable) {
        this(Comment.class, forVariable(variable), INITS);
    }

    public QComment(Path<? extends Comment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QComment(PathMetadata metadata, PathInits inits) {
        this(Comment.class, metadata, inits);
    }

    public QComment(Class<? extends Comment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new com.goodjob.article.domain.article.entity.QArticle(forProperty("article"), inits.get("article")) : null;
        this.member = inits.isInitialized("member") ? new com.goodjob.member.entity.QMember(forProperty("member")) : null;
    }

}

