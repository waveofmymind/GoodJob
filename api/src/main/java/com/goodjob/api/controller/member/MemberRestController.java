package com.goodjob.api.controller.member;

import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.common.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/join/valid/username")
    public RsData<String> checkDuplicateUsername(String username) {
        if (username.length() < 4) {
            return RsData.of("F-1", "4자 이상 입력하세요.");
        }

        Optional<Member> opMember = memberService.findByUsername(username);
        if (opMember.isPresent()) {
            return RsData.of("F-1", "이미 사용중인 아이디입니다.");
        }

        return RsData.of("S-1", "사용 가능한 아이디입니다.");
    }

    @PostMapping("/join/valid/nickname")
    public RsData<String> checkDuplicateNickname(String nickname) {
        if (nickname.length() < 2) {
            return RsData.of("F-1", "2자 이상 입력하세요.");
        }

        Optional<Member> opMember = memberService.findByNickname(nickname);
        if (opMember.isPresent()) {
            return RsData.of("F-1", "이미 사용중인 닉네임입니다.");
        }

        return RsData.of("S-1", "사용 가능한 닉네임입니다.");
    }
}
