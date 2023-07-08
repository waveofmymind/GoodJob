package com.goodjob.api.controller.member;

import com.goodjob.core.domain.email.service.EmailVerificationService;
import com.goodjob.core.domain.member.dto.request.EditRequestDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.core.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/recover")
public class MemberRecoverController {

    private final MemberService memberService;
    private final EmailVerificationService emailVerificationService;

    @GetMapping("/username")
    @PreAuthorize("isAnonymous()")
    public String showRecoverUsername() {
        return "member/recover-username";
    }

    @PostMapping("/username")
    @PreAuthorize("isAnonymous()")
    @ResponseBody
    public RsData<String> recoverUsername(String email) {
        Optional<Member> opMember = memberService.findByEmail(email);

        if (opMember.isEmpty()) {
            return RsData.of("F-1", "해당 이메일을 가진 회원이 없습니다.");
        }

        String nickname = opMember.get().getNickname();
        String filteredNickname = nickname.substring(0, nickname.length() - 2) + "**";
        String msg = "가입하신 아이디는 %s 입니다.".formatted(filteredNickname);

        return RsData.of("S-1", msg);
    }

    @GetMapping("/password")
    @PreAuthorize("isAnonymous()")
    public String showRecoverPassword() {
        return "member/recover-password";
    }

    @PostMapping("/password")
    @PreAuthorize("isAnonymous()")
    @ResponseBody
    public RsData<String> recoverPassword(String username) {
        // 아이디 일치여부 확인 후 이메일전송
        Optional<Member> opMember = memberService.findByUsername(username);

        if (opMember.isEmpty()) {
            return RsData.of("F-1", "존재하지 않는 아이디입니다.");
        }

        Member member = opMember.get();

        // 임시비밀번호 생성 후 회원 비밀번호 변경
        EditRequestDto editRequestDto = memberService.genEditRequestDtoWithTempPassword(member);
        RsData updateRsData = memberService.update(member, editRequestDto);

        if (updateRsData.isFail()) {
            return updateRsData;
        }

        // 메일 전송
        emailVerificationService.sendPassword(member.getEmail(), editRequestDto.getPassword());

        return RsData.of("S-1", "가입하신 메일주소로 임시비밀번호가 발송되었습니다.");
    }
}
