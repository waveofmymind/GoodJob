package com.goodjob.core.domain.chat.dto;


import com.goodjob.core.domain.chat.entity.ChatMessage;
import com.goodjob.core.domain.chat.entity.ChatRoom;
import com.goodjob.core.domain.member.entity.Member;
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
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public static ChatRoomDetailDTO toChatRoomDetailDTO(ChatRoom chatRoom){
        ChatRoomDetailDTO chatRoomDetailDTO = new ChatRoomDetailDTO();

        List<ChatMessage> chatList = chatRoom.getChatList();

        chatRoomDetailDTO.setChatRoomId(chatRoom.getId());
        chatRoomDetailDTO.setSender(chatRoom.getSender());
        chatRoomDetailDTO.setReceiver(chatRoom.getReceiver());
        chatRoomDetailDTO.setRoomId(chatRoom.getRoomId());



        if (chatList.size() == 0) {
            chatRoomDetailDTO.setLastMessage("입장해서 채팅하세요!");
            chatRoomDetailDTO.setLastMessageTime(LocalDateTime.now());
        }

        else {
            chatRoomDetailDTO.setLastMessage(chatList.get(chatList.size() - 1).getMessage());
            chatRoomDetailDTO.setLastMessageTime(chatList.get(chatList.size()-1).getCreatedDate());
        }


        return chatRoomDetailDTO;
    }

}
