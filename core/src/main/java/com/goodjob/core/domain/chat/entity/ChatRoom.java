package com.goodjob.core.domain.chat.entity;


import com.goodjob.core.domain.BaseEntity;
import com.goodjob.core.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String roomId;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ChatMessage> chatList = new ArrayList<>();
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoom", cascade = CascadeType.ALL)
//    @Builder.Default
//    private List<ChatMember> chatMembers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Setter
    private int status;

    private LocalDateTime reservationTime;

    public static ChatRoom create(Member sender, Member receiver, String date, String time) {
        String[] dateArr = date.split("-");
        int year = Integer.parseInt(dateArr[0]);
        int month = Integer.parseInt(dateArr[1]);
        int day = Integer.parseInt(dateArr[2]);

        return ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .sender(sender)
                .receiver(receiver)
                .chatList(new ArrayList<>())
                .status(0)
                .reservationTime(LocalDateTime.of(year, month, day, Integer.parseInt(time), 0, 0))
                .build();
    }

    public void addMessage(ChatMessage chatMessage) {
        chatList.add(chatMessage);
    }


    public String getStatusDisplayName() {
        return switch (status) {
            case 1 -> "수락";
            case 2 -> "거절";
            case 3 -> "종료";
            default -> "대기중";
        };
    }
}
