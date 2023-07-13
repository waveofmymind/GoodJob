package com.goodjob.api.controller.chat;

import com.goodjob.api.controller.chat.controller.ChatController;
import com.goodjob.common.rsData.RsData;

import com.goodjob.member.entity.Member;
import com.goodjob.member.service.MemberService;
import com.goodjob.mentoring.domain.chat.entity.ChatRoom;
import com.goodjob.mentoring.domain.chat.service.ChatService;
import com.goodjob.mentoring.domain.mentoring.entity.Mentoring;
import com.goodjob.mentoring.domain.mentoring.service.MentoringService;
import com.goodjob.core.global.rq.Rq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.goodjob.member.constant.Membership.*;
import static com.goodjob.member.constant.ProviderType.GOODJOB;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {
    @InjectMocks
    private ChatController chatController;

    @Mock
    private ChatService chatService;

    @Mock
    private MemberService memberService;
    @Mock
    private MentoringService mentoringService;

    @Mock
    private Rq rq;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
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
    @DisplayName("채팅방 개설")
    void t001() throws Exception {
        // Given
        Member premiumMember = premiumMember();
        Member mentorMember = mentorMember();
        ChatRoom chatRoom = createChatRoom();

        Mentoring mentoring = Mentoring.builder()
                .member(mentorMember)
                .title("title test")
                .content("content test")
                .job("백엔드")
                .career("신입(1년 이하)")
                .currentJob("네이버")
                .preferredTime("자유")
                .build();

        RsData<Mentoring> mentoringRsData = new RsData<>("S-1", "멘토링을 성공적으로 가져왔습니다.", mentoring);

        doReturn(premiumMember).when(rq).getMember();
        doReturn(Optional.of(premiumMember)).when(memberService).findByUsername(any(String.class));
        doReturn(mentoringRsData).when(mentoringService).findById(anyLong());
        doReturn(false).when(chatService).isExistChatRoom(anyLong(), anyLong());
        doReturn(chatRoom).when(chatService).createChatRoom(any(Member.class), any(Member.class), any(String.class), any(String.class));
        doReturn("redirect:/chat/rooms").when(rq).redirectWithMsg(any(String.class), any(String.class));

        // When
        ResultActions resultActions = mockMvc
                .perform(post("/chat/room/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("date", "2023-07-10")
                        .param("time", "12")
                )
                .andDo(MockMvcResultHandlers.print());

        // Then
        resultActions
                .andExpect(handler().handlerType(ChatController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/chat/rooms/**"));
    }

    @Test
    @DisplayName("채팅방 삭제")
    void t002() throws Exception {
        // Given
        Member premiumMember = premiumMember();

        doReturn(premiumMember).when(rq).getMember();
        doReturn(Optional.of(premiumMember)).when(memberService).findByUsername(any(String.class));

        // When
        ResultActions resultActions = mockMvc
                .perform(get("/chat/delete/room")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(MockMvcResultHandlers.print());

        // Then
        resultActions
                .andExpect(handler().handlerType(ChatController.class))
                .andExpect(handler().methodName("deleteRoom"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/chat/rooms/**"));
    }
}
