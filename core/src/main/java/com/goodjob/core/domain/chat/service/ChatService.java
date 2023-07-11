package com.goodjob.core.domain.chat.service;

import com.goodjob.core.domain.chat.dto.ChatMessageDTO;
import com.goodjob.core.domain.chat.dto.ChatRoomDetailDTO;
import com.goodjob.core.domain.chat.entity.ChatMessage;
import com.goodjob.core.domain.chat.entity.ChatRoom;
import com.goodjob.core.domain.chat.repository.ChatMessageRepository;
import com.goodjob.core.domain.chat.repository.ChatRoomRepository;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.core.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberService memberService;

    @Transactional
    public ChatRoom createChatRoom(Member sender, Member receiver, String date, String time) {
        ChatRoom chatRoom = ChatRoom.create(sender, receiver, date, time);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    public ChatRoom findRoomById(String id) {
        return chatRoomRepository.findByRoomId(id).orElse(null);
    }


    @Transactional
    public void createChatMessage(ChatMessageDTO chatMessageDTO) {
        String message = chatMessageDTO.getMessage();
        String roomId = chatMessageDTO.getRoomId();
        String writer = chatMessageDTO.getWriter();

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElse(null);
        Member sender = memberService.findByNickname(writer).orElse(null);

        ChatMessage chatMessage = ChatMessage.create(message, sender, chatRoom);
        chatRoom.addMessage(chatMessage);
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findByRoomId(String roomId) {
        return chatMessageRepository.findByChatRoom_RoomId(roomId);
    }

    public List<ChatRoomDetailDTO> findByUsername(Member member) {

        String username = member.getUsername();

        List<ChatRoom> chatRooms = chatRoomRepository.findByChatRoom_Username(username);

        List<ChatRoomDetailDTO> chatRoomDetailDTOS = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            chatRoomDetailDTOS.add(ChatRoomDetailDTO.toChatRoomDetailDTO(chatRoom));
        }

        return chatRoomDetailDTOS;
    }

    public ChatRoom findExistChatRoom(Long senderId, Long receiverId) {
        return chatRoomRepository.findExistChatRoom(senderId, receiverId).orElse(null);
    }

    public Boolean isExistChatRoom(Long senderId, Long receiverId) {
        if (findExistChatRoom(senderId, receiverId) == null) {
            return false;
        }

        return true;
    }

    @Transactional
    public RsData deleteRoom(String roomId, Member member) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElse(null);
        if(member.getNickname().equals(chatRoom.getSender().getNickname())) {
            chatRoomRepository.delete(chatRoom);
        } else if(member.getNickname().equals((chatRoom.getReceiver().getNickname()))) {
            chatRoom.setDeleted(true);
            chatRoomRepository.save(chatRoom);
            return RsData.of("S-2", "채팅방이 삭제되었습니다.", chatRoom);
        }
        return RsData.of("S-1", "채팅방이 삭제되었습니다.");
    }

    @Transactional
    public void permitRoom(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElse(null);
        chatRoom.setStatus(1);
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void rejectRoom(String roomId, String content) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElse(null);
        chatRoom.setStatus(2);
        chatRoom.setRejectionReason(content);
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public RsData quitRoom(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElse(null);
        chatRoom.setStatus(3);
        chatRoomRepository.save(chatRoom);

        return RsData.of("S-1", "채팅을 종료합니다.", chatRoom);
    }
}
