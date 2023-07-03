package com.goodjob.core.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1772029959L;

    public static final QMember member = new QMember("member1");

    public final com.goodjob.core.domain.QBaseEntity _super = new com.goodjob.core.domain.QBaseEntity(this);

    public final ListPath<com.goodjob.core.domain.article.entity.Article, com.goodjob.core.domain.article.entity.QArticle> articles = this.<com.goodjob.core.domain.article.entity.Article, com.goodjob.core.domain.article.entity.QArticle>createList("articles", com.goodjob.core.domain.article.entity.Article.class, com.goodjob.core.domain.article.entity.QArticle.class, PathInits.DIRECT2);

    public final NumberPath<Integer> coin = createNumber("coin", Integer.class);

    public final ListPath<com.goodjob.core.domain.comment.entity.Comment, com.goodjob.core.domain.comment.entity.QComment> comments = this.<com.goodjob.core.domain.comment.entity.Comment, com.goodjob.core.domain.comment.entity.QComment>createList("comments", com.goodjob.core.domain.comment.entity.Comment.class, com.goodjob.core.domain.comment.entity.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath providerType = createString("providerType");

    public final StringPath username = createString("username");

    public final StringPath userRole = createString("userRole");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

