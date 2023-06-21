package com.goodjob.batch.job.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QJobStatistic is a Querydsl query type for JobStatistic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobStatistic extends EntityPathBase<JobStatistic> {

    private static final long serialVersionUID = -838349378L;

    public static final QJobStatistic jobStatistic = new QJobStatistic("jobStatistic");

    public final NumberPath<Integer> career = createNumber("career", Integer.class);

    public final StringPath company = createString("company");

    public final StringPath deadLine = createString("deadLine");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath sector = createString("sector");

    public final NumberPath<Integer> sectorCode = createNumber("sectorCode", Integer.class);

    public final StringPath startDate = createString("startDate");

    public final StringPath subject = createString("subject");

    public final StringPath url = createString("url");

    public QJobStatistic(String variable) {
        super(JobStatistic.class, forVariable(variable));
    }

    public QJobStatistic(Path<? extends JobStatistic> path) {
        super(path.getType(), path.getMetadata());
    }

    public QJobStatistic(PathMetadata metadata) {
        super(JobStatistic.class, metadata);
    }

}

