package com.goodjob.article.domain.hashTag.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHashTag is a Querydsl query type for HashTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHashTag extends EntityPathBase<HashTag> {

    private static final long serialVersionUID = -1334859462L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHashTag hashTag = new QHashTag("hashTag");

    public final com.goodjob.article.domain.article.entity.QArticle article;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.goodjob.article.domain.keyword.entity.QKeyword keyword;

    public QHashTag(String variable) {
        this(HashTag.class, forVariable(variable), INITS);
    }

    public QHashTag(Path<? extends HashTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHashTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHashTag(PathMetadata metadata, PathInits inits) {
        this(HashTag.class, metadata, inits);
    }

    public QHashTag(Class<? extends HashTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new com.goodjob.article.domain.article.entity.QArticle(forProperty("article"), inits.get("article")) : null;
        this.keyword = inits.isInitialized("keyword") ? new com.goodjob.article.domain.keyword.entity.QKeyword(forProperty("keyword")) : null;
    }

}

