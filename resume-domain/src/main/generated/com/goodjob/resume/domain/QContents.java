package com.goodjob.resume.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContents is a Querydsl query type for Contents
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QContents extends BeanPath<Contents> {

    private static final long serialVersionUID = -595126466L;

    public static final QContents contents1 = new QContents("contents1");

    public final ListPath<Content, QContent> contents = this.<Content, QContent>createList("contents", Content.class, QContent.class, PathInits.DIRECT2);

    public QContents(String variable) {
        super(Contents.class, forVariable(variable));
    }

    public QContents(Path<? extends Contents> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContents(PathMetadata metadata) {
        super(Contents.class, metadata);
    }

}

