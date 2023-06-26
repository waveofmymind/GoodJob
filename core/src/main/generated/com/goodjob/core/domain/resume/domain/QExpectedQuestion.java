package com.goodjob.core.domain.resume.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExpectedQuestion is a Querydsl query type for ExpectedQuestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExpectedQuestion extends EntityPathBase<ExpectedQuestion> {

    private static final long serialVersionUID = 992627133L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExpectedQuestion expectedQuestion = new QExpectedQuestion("expectedQuestion");

    public final QAnswers answers;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.goodjob.core.domain.member.entity.QMember member;

    public final QQuestions questions;

    public QExpectedQuestion(String variable) {
        this(ExpectedQuestion.class, forVariable(variable), INITS);
    }

    public QExpectedQuestion(Path<? extends ExpectedQuestion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExpectedQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExpectedQuestion(PathMetadata metadata, PathInits inits) {
        this(ExpectedQuestion.class, metadata, inits);
    }

    public QExpectedQuestion(Class<? extends ExpectedQuestion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.answers = inits.isInitialized("answers") ? new QAnswers(forProperty("answers")) : null;
        this.member = inits.isInitialized("member") ? new com.goodjob.core.domain.member.entity.QMember(forProperty("member")) : null;
        this.questions = inits.isInitialized("questions") ? new QQuestions(forProperty("questions")) : null;
    }

}

