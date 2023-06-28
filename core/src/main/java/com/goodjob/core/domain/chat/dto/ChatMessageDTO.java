package com.goodjob.core.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {

    private String roomId;
    private String writer;

    private String message;

    public static ChatMessageDTO create(String roomId, String writer, String message) {
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setRoomId(roomId);
        chatMessageDTO.setWriter(writer);
        chatMessageDTO.setMessage(message);

        return chatMessageDTO;
    }
}