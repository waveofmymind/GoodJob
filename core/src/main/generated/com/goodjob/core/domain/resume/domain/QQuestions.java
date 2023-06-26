package com.goodjob.core.domain.resume.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestions is a Querydsl query type for Questions
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QQuestions extends BeanPath<Questions> {

    private static final long serialVersionUID = 93550382L;

    public static final QQuestions questions1 = new QQuestions("questions1");

    public final ListPath<String, StringPath> questions = this.<String, StringPath>createList("questions", String.class, StringPath.class, PathInits.DIRECT2);

    public QQuestions(String variable) {
        super(Questions.class, forVariable(variable));
    }

    public QQuestions(Path<? extends Questions> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestions(PathMetadata metadata) {
        super(Questions.class, metadata);
    }

}

