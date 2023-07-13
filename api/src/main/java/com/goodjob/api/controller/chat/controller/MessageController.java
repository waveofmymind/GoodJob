package com.goodjob.api.controller.chat.controller;



import com.goodjob.common.rsData.RsData;
import com.goodjob.mentoring.domain.chat.dto.ChatMessageDTO;
import com.goodjob.mentoring.domain.chat.entity.ChatRoom;
import com.goodjob.mentoring.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
