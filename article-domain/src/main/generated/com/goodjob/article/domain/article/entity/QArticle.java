package com.goodjob.article.domain.article.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticle is a Querydsl query type for Article
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticle extends EntityPathBase<Article> {

    private static final long serialVersionUID = 1907796986L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticle article = new QArticle("article");

    public final com.goodjob.common.QBaseEntity _super = new com.goodjob.common.QBaseEntity(this);

    public final NumberPath<Integer> category = createNumber("category", Integer.class);

    public final ListPath<com.goodjob.article.domain.comment.entity.Comment, com.goodjob.article.domain.comment.entity.QComment> commentList = this.<com.goodjob.article.domain.comment.entity.Comment, com.goodjob.article.domain.comment.entity.QComment>createList("commentList", com.goodjob.article.domain.comment.entity.Comment.class, com.goodjob.article.domain.comment.entity.QComment.class, PathInits.DIRECT2);

    public final NumberPath<Long> commentsCount = createNumber("commentsCount", Long.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final ListPath<com.goodjob.article.domain.file.entity.File, com.goodjob.article.domain.file.entity.QFile> fileList = this.<com.goodjob.article.domain.file.entity.File, com.goodjob.article.domain.file.entity.QFile>createList("fileList", com.goodjob.article.domain.file.entity.File.class, com.goodjob.article.domain.file.entity.QFile.class, PathInits.DIRECT2);

    public final ListPath<com.goodjob.article.domain.hashTag.entity.HashTag, com.goodjob.article.domain.hashTag.entity.QHashTag> hashTagList = this.<com.goodjob.article.domain.hashTag.entity.HashTag, com.goodjob.article.domain.hashTag.entity.QHashTag>createList("hashTagList", com.goodjob.article.domain.hashTag.entity.HashTag.class, com.goodjob.article.domain.hashTag.entity.QHashTag.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final ListPath<com.goodjob.article.domain.likes.entity.Likes, com.goodjob.article.domain.likes.entity.QLikes> likesList = this.<com.goodjob.article.domain.likes.entity.Likes, com.goodjob.article.domain.likes.entity.QLikes>createList("likesList", com.goodjob.article.domain.likes.entity.Likes.class, com.goodjob.article.domain.likes.entity.QLikes.class, PathInits.DIRECT2);

    public final com.goodjob.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath title = createString("title");

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QArticle(String variable) {
        this(Article.class, forVariable(variable), INITS);
    }

    public QArticle(Path<? extends Article> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticle(PathMetadata metadata, PathInits inits) {
        this(Article.class, metadata, inits);
    }

    public QArticle(Class<? extends Article> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.goodjob.member.entity.QMember(forProperty("member")) : null;
    }

}

