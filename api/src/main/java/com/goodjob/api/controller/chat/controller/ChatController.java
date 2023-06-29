package com.goodjob.api.controller.chat.controller;


import com.goodjob.core.domain.chat.dto.ChatRoomDetailDTO;
import com.goodjob.core.domain.chat.entity.ChatMessage;
import com.goodjob.core.domain.chat.service.ChatService;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.core.domain.mentoring.entity.Mentoring;
import com.goodjob.core.domain.mentoring.service.MentoringService;
import com.goodjob.core.global.base.rsData.RsData;
import com.goodjob.core.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final MemberService memberService;
    private final MentoringService mentoringService;
    private final Rq rq;

    //나의 채팅방 목록 조회
    @GetMapping("/rooms")
    public String myRooms(@AuthenticationPrincipal User user, Model model) {
        String username = user.getUsername();
        List<ChatRoomDetailDTO> chatRooms = chatService.findByUsername(username);
        model.addAttribute("myNickname", rq.getMember().getNickname());
        model.addAttribute("list", chatRooms);
        return "chat/rooms";
    }

    //채팅방 개설
    @PostMapping("/room/{id}")
    public String create(@PathVariable Long id, @AuthenticationPrincipal User user, Model model){

        Member member1 = memberService.findByUsername(user.getUsername()).orElse(null);
        RsData<Mentoring> mentoringRsData = mentoringService.findById(id);
        if(mentoringRsData.isFail()) {
            return rq.historyBack(mentoringRsData);
        }
        Mentoring mentoring = mentoringRsData.getData();
        Member member2 = mentoring.getMember();

        if (member1.getId() == member2.getId()) {
            return rq.historyBack("본인이 올린 글에 커피챗을 신청할 수 없습니다.");
        }

        if (chatService.isExistChatRoom(member1.getId(), member2.getId())) {
            return rq.historyBack("이미 판매자와의 채팅방이 존재합니다. ");
        }

        model.addAttribute("room", chatService.createChatRoom(member1, member2));

        return rq.redirectWithMsg("/chat/rooms", "커피챗이 신청되었습니다.");
    }

    //채팅방 상세
    @GetMapping("/room")
    public String getRoom(String roomId, Model model, @AuthenticationPrincipal User user){

        List<ChatMessage> messages = chatService.findByRoomId(roomId);
        Member member = memberService.findByUsername(user.getUsername()).orElse(null);

        if (member == null) {
            return rq.redirectWithMsg("/user/member/login", "다시 로그인해주세요!");
        }


        model.addAttribute("room", chatService.findRoomById(roomId));
        model.addAttribute("messages", messages);
        model.addAttribute("member", member);
        model.addAttribute("admin", "관리자");

        return "chat/room";
    }

    //채팅방 삭제
    @GetMapping("/delete/room")
    public String deleteRoom(String roomId, Model model, @AuthenticationPrincipal User user){

        chatService.deleteRoom(roomId);

        return "redirect:/chat/rooms";
    }

    //채팅방 수락
    @GetMapping("/permit/room")
    public String permitRoom(String roomId, Model model, @AuthenticationPrincipal User user){
        chatService.permitRoom(roomId);

        return "redirect:/chat/rooms";
    }

    //채팅방 거절
    @GetMapping("/reject/room")
    public String rejectRoom(String roomId, Model model, @AuthenticationPrincipal User user){
        chatService.rejectRoom(roomId);
        return "redirect:/chat/rooms";

    }


}
