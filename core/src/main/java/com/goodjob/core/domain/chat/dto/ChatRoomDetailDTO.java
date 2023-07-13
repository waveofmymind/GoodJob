package com.goodjob.core.domain.chat.dto;


import com.goodjob.core.domain.chat.entity.ChatMessage;
import com.goodjob.core.domain.chat.entity.ChatRoom;
import com.goodjob.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailDTO {
    private Long chatRoomId;

    private Member sender;
    private Member receiver;
    private String roomId;
    private String name;
    private int status;
    private LocalDateTime reservationTime;
    private boolean isDeleted;
    private String rejectionReason;

    public static ChatRoomDetailDTO toChatRoomDetailDTO(ChatRoom chatRoom){
        ChatRoomDetailDTO chatRoomDetailDTO = new ChatRoomDetailDTO();

        List<ChatMessage> chatList = chatRoom.getChatList();

        chatRoomDetailDTO.setChatRoomId(chatRoom.getId());
        chatRoomDetailDTO.setSender(chatRoom.getSender());
        chatRoomDetailDTO.setReceiver(chatRoom.getReceiver());
        chatRoomDetailDTO.setRoomId(chatRoom.getRoomId());
        chatRoomDetailDTO.setStatus(chatRoom.getStatus());
        chatRoomDetailDTO.setReservationTime(chatRoom.getReservationTime());
        chatRoomDetailDTO.setDeleted(chatRoom.isDeleted());
        chatRoomDetailDTO.setRejectionReason(chatRoom.getRejectionReason());

        return chatRoomDetailDTO;
    }

}
