package com.goodjob.resume.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTitles is a Querydsl query type for Titles
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QTitles extends BeanPath<Titles> {

    private static final long serialVersionUID = 1888525887L;

    public static final QTitles titles1 = new QTitles("titles1");

    public final ListPath<String, StringPath> titles = this.<String, StringPath>createList("titles", String.class, StringPath.class, PathInits.DIRECT2);

    public QTitles(String variable) {
        super(Titles.class, forVariable(variable));
    }

    public QTitles(Path<? extends Titles> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTitles(PathMetadata metadata) {
        super(Titles.class, metadata);
    }

}

