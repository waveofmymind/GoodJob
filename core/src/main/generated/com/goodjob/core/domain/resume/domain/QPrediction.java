package com.goodjob.core.domain.resume.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPrediction is a Querydsl query type for Prediction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPrediction extends EntityPathBase<Prediction> {

    private static final long serialVersionUID = -818966834L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPrediction prediction = new QPrediction("prediction");

    public final com.goodjob.core.domain.QBaseEntity _super = new com.goodjob.core.domain.QBaseEntity(this);

    public final QContents contents;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> member = createNumber("member", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final EnumPath<ServiceType> serviceType = createEnum("serviceType", ServiceType.class);

    public final QTitles titles;

    public QPrediction(String variable) {
        this(Prediction.class, forVariable(variable), INITS);
    }

    public QPrediction(Path<? extends Prediction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPrediction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPrediction(PathMetadata metadata, PathInits inits) {
        this(Prediction.class, metadata, inits);
    }

    public QPrediction(Class<? extends Prediction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contents = inits.isInitialized("contents") ? new QContents(forProperty("contents")) : null;
        this.titles = inits.isInitialized("titles") ? new QTitles(forProperty("titles")) : null;
    }

}

