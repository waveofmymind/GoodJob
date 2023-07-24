package com.goodjob.member.service;


import com.goodjob.member.dto.request.EditRequestDto;
import com.goodjob.member.entity.Member;
import com.goodjob.member.repository.MemberRepository;
import com.goodjob.common.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberEditService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    private final MemberService memberService;

    public RsData<String> verifyPassword(String passwordToEdit, String memberPassword) {
        if (!passwordEncoder.matches(passwordToEdit, memberPassword)) {
            return RsData.of("F-1", "비밀번호가 일치하지않습니다.");
        }

        return RsData.of("S-1", "비밀번호가 일치합니다. 회원정보수정 페이지로 이동합니다.");
    }

    // 닉네임, 비밀번호 중 하나 이상 바꿔야 회원정보 수정 가능
    @Transactional
    public RsData update(Member member, EditRequestDto editRequestDto) {
        String nickname = editRequestDto.getNickname().replaceAll("\\s+", "");
        RsData nicknameVerificationRsData = verifyProvidedNickname(member.getNickname(), nickname);

        if (nicknameVerificationRsData.isFail()) {
            return nicknameVerificationRsData;
        }

        if (!member.isSocialMember()) {
            boolean matches = passwordEncoder.matches(editRequestDto.getPassword(), member.getPassword());

            // 수정값 없는 경우
            if (member.getNickname().equals(nickname) && matches) {
                return RsData.of("F-1", "수정된 정보가 없습니다!");
            }

            String password = passwordEncoder.encode(editRequestDto.getPassword());

            member.updatePassword(password);
        } else {
            if (member.getNickname().equals(nickname)) {
                return RsData.of("F-1", "수정된 정보가 없습니다!");
            }
        }

        member.updateNickname(nickname);
        memberRepository.save(member);

        return RsData.of("S-1", "회원 정보가 수정되었습니다.");
    }

    public EditRequestDto getEditRequestDto(Member member) {
        String tempPassword = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15);
        EditRequestDto editRequestDto = EditRequestDto.builder()
                .nickname(member.getNickname())
                .password(tempPassword)
                .build();

        return editRequestDto;
    }

    private RsData verifyProvidedNickname(String originalNickname, String providedNickname) {
        Optional<Member> opMember = memberService.findByNickname(providedNickname);

        // 이미 존재하는 닉네임이면서 현재 닉네임과 수정하려는 닉네임이 같지 않은 경우
        if (opMember.isPresent() && !originalNickname.equals(providedNickname)) {
            return RsData.of("F-1", "이미 존재하는 닉네임 입니다.");
        }

        return RsData.of("S-1", "수정 가능한 닉네임 입니다.");
    }
}