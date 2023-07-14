package com.goodjob.mentoring.domain.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = -1788965046L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final com.goodjob.common.QBaseEntity _super = new com.goodjob.common.QBaseEntity(this);

    public final ListPath<ChatMessage, QChatMessage> chatList = this.<ChatMessage, QChatMessage>createList("chatList", ChatMessage.class, QChatMessage.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final com.goodjob.member.entity.QMember receiver;

    public final StringPath rejectionReason = createString("rejectionReason");

    public final DateTimePath<java.time.LocalDateTime> reservationTime = createDateTime("reservationTime", java.time.LocalDateTime.class);

    public final StringPath roomId = createString("roomId");

    public final com.goodjob.member.entity.QMember sender;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QChatRoom(String variable) {
        this(ChatRoom.class, forVariable(variable), INITS);
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoom(PathMetadata metadata, PathInits inits) {
        this(ChatRoom.class, metadata, inits);
    }

    public QChatRoom(Class<? extends ChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiver = inits.isInitialized("receiver") ? new com.goodjob.member.entity.QMember(forProperty("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new com.goodjob.member.entity.QMember(forProperty("sender")) : null;
    }

}

