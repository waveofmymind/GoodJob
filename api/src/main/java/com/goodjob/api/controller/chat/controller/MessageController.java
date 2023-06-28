package com.goodjob.api.controller.chat.controller;


import com.goodjob.core.domain.chat.dto.ChatMessageDTO;
import com.goodjob.core.domain.chat.entity.ChatMessage;
import com.goodjob.core.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDTO message){

        String roomId = message.getRoomId();

        List<ChatMessage> messages = chatService.findByRoomId(roomId);

        if (messages.size() == 0) {
            message.setMessage(message.getWriter() + "님이 물건 구매를 원합니다.");
            chatService.createChatMessage(message);
            template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
            return;
        }
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message){

        chatService.createChatMessage(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
