package com.goodjob.api.controller.chat.controller;


import com.goodjob.core.domain.chat.dto.ChatMessageDTO;
import com.goodjob.core.domain.chat.entity.ChatMessage;
import com.goodjob.core.domain.chat.entity.ChatRoom;
import com.goodjob.core.domain.chat.service.ChatService;
import com.goodjob.core.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;


    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message){

        chatService.createChatMessage(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/quit")
    public void quit(ChatMessageDTO message){
        RsData<ChatRoom> chatRoomRsData = chatService.quitRoom(message.getRoomId());
        template.convertAndSend("/sub/chat/quit/" + message.getRoomId(), chatRoomRsData.getData().getStatus());
    }
}
