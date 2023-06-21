package com.goodjob.core.domain.email.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSendEmailLog is a Querydsl query type for SendEmailLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendEmailLog extends EntityPathBase<SendEmailLog> {

    private static final long serialVersionUID = 642572595L;

    public static final QSendEmailLog sendEmailLog = new QSendEmailLog("sendEmailLog");

    public final com.goodjob.core.domain.QBaseEntity _super = new com.goodjob.core.domain.QBaseEntity(this);

    public final StringPath body = createString("body");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> failDate = createDateTime("failDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath resultCode = createString("resultCode");

    public final DateTimePath<java.time.LocalDateTime> sendEndDate = createDateTime("sendEndDate", java.time.LocalDateTime.class);

    public final StringPath subject = createString("subject");

    public final StringPath verificationCode = createString("verificationCode");

    public QSendEmailLog(String variable) {
        super(SendEmailLog.class, forVariable(variable));
    }

    public QSendEmailLog(Path<? extends SendEmailLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSendEmailLog(PathMetadata metadata) {
        super(SendEmailLog.class, metadata);
    }

}

