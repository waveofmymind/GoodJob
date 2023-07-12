package com.goodjob.core.domain.chat.service;

import com.goodjob.core.domain.chat.dto.ChatRoomDetailDTO;
import com.goodjob.core.domain.chat.entity.ChatRoom;
import com.goodjob.core.domain.chat.repository.ChatMessageRepository;
import com.goodjob.core.domain.chat.repository.ChatRoomRepository;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.common.rsData.RsData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.goodjob.core.domain.member.constant.Membership.*;
import static com.goodjob.core.domain.member.constant.ProviderType.GOODJOB;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(chatService);
    }

    private Member premiumMember() {
        return Member.builder()
                .id(2L)
                .username("test2")
                .password("1234")
                .nickname("tester2")
                .email("test2@naver.com")
                .membership(PREMIUM)
                .providerType(GOODJOB)
                .build();
    }

    private Member mentorMember() {
        return Member.builder()
                .id(3L)
                .username("test3")
                .password("1234")
                .nickname("tester3")
                .email("test3@naver.com")
                .membership(MENTOR)
                .providerType(GOODJOB)
                .build();
    }

    private ChatRoom createChatRoom() {
        Member premiumMember = premiumMember();
        Member mentorMember = mentorMember();
        return ChatRoom.create(premiumMember, mentorMember, "2023-07-10", "12");
    }

    @Test
    @DisplayName("채팅방 만들기")
    void t001() {
        //GIVEN
        Member sender = premiumMember();
        Member receiver = mentorMember();
        String date = "2023-07-11";
        String time = "12";

        //WHEN
        ChatRoom chatRoom = chatService.createChatRoom(sender, receiver, date, time);

        //THEN
        assertThat(chatRoom.getSender()).isEqualTo(sender);
        assertThat(chatRoom.getReservationTime()).isEqualTo(LocalDateTime.of(2023, 7, 11, 12, 0, 0));
    }

    @Test
    @DisplayName("유저 이름으로 채팅방 찾기")
    void t002() {
        //GIVEN
        Member sender = premiumMember();
        ChatRoom chatRoom = createChatRoom();
        List<ChatRoom> chatRoomList = new ArrayList<>();
        chatRoomList.add(chatRoom);

        doReturn(chatRoomList).when(chatRoomRepository).findByChatRoom_Username(any(String.class));

        //WHEN
        List<ChatRoomDetailDTO> chatRoomDetailDTOList = chatService.findByUsername(sender);

        //THEN
        assertThat(chatRoomDetailDTOList.size()).isEqualTo(1);
        assertThat(chatRoomDetailDTOList.get(0).getSender().getUsername()).isEqualTo(sender.getUsername());
    }

    @Test
    @DisplayName("이미 존재하는 방일 경우 true 반환")
    void t003() {
        //GIVEN
        Member sender = premiumMember();
        Member receiver = mentorMember();
        ChatRoom chatRoom = createChatRoom();

        doReturn(Optional.of(chatRoom)).when(chatRoomRepository).findExistChatRoom(anyLong(), anyLong());

        //WHEN
        boolean isExistChatRoom = chatService.isExistChatRoom(sender.getId(), receiver.getId());

        //THEN
        assertThat(isExistChatRoom).isEqualTo(true);
    }

    @Test
    @DisplayName("채팅방이 존재하지 않을 경우 false 반환")
    void t004() {
        //GIVEN
        Member sender = premiumMember();
        Member receiver = mentorMember();

        //WHEN
        boolean isExistChatRoom = chatService.isExistChatRoom(sender.getId(), receiver.getId());

        //THEN
        assertThat(isExistChatRoom).isEqualTo(false);
    }

    @Test
    @DisplayName("채팅방 삭제(sender 측에서 삭제)")
    void t005() {
        //GIVEN
        Member sender = premiumMember();
        Member receiver = mentorMember();
        ChatRoom chatRoom = createChatRoom();

        doReturn(Optional.of(chatRoom)).when(chatRoomRepository).findByRoomId(any(String.class));

        //WHEN
        RsData<ChatRoom> chatRoomRsData = chatService.deleteRoom(chatRoom.getRoomId(), sender);

        //THEN
        assertThat(chatRoomRsData.getResultCode()).isEqualTo("S-1");
    }

    @Test
    @DisplayName("채팅방 삭제(receiver 측에서 삭제)")
    void t006() {
        //GIVEN
        Member sender = premiumMember();
        Member receiver = mentorMember();
        ChatRoom chatRoom = createChatRoom();

        doReturn(Optional.of(chatRoom)).when(chatRoomRepository).findByRoomId(any(String.class));

        //WHEN
        RsData<ChatRoom> chatRoomRsData = chatService.deleteRoom(chatRoom.getRoomId(), receiver);

        //THEN
        assertThat(chatRoomRsData.getResultCode()).isEqualTo("S-2");
        assertThat(chatRoomRsData.getData().isDeleted()).isEqualTo(true);
    }
}
