package com.goodjob.resume.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTitle is a Querydsl query type for Title
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTitle extends EntityPathBase<Title> {

    private static final long serialVersionUID = 2000582836L;

    public static final QTitle title1 = new QTitle("title1");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public QTitle(String variable) {
        super(Title.class, forVariable(variable));
    }

    public QTitle(Path<? extends Title> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTitle(PathMetadata metadata) {
        super(Title.class, metadata);
    }

}

