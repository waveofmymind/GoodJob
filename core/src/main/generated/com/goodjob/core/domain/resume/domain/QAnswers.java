package com.goodjob.core.domain.resume.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnswers is a Querydsl query type for Answers
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QAnswers extends BeanPath<Answers> {

    private static final long serialVersionUID = 1769075350L;

    public static final QAnswers answers1 = new QAnswers("answers1");

    public final ListPath<String, StringPath> answers = this.<String, StringPath>createList("answers", String.class, StringPath.class, PathInits.DIRECT2);

    public QAnswers(String variable) {
        super(Answers.class, forVariable(variable));
    }

    public QAnswers(Path<? extends Answers> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAnswers(PathMetadata metadata) {
        super(Answers.class, metadata);
    }

}

