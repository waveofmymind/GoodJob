package com.goodjob.mentoring.domain.chat.entity;


import com.goodjob.common.BaseEntity;
import com.goodjob.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    public static ChatMessage create(String message, Member sender, ChatRoom chatRoom) {
        return ChatMessage.builder()
                .message(message)
                .sender(sender)
                .chatRoom(chatRoom)
                .build();
    }
}
