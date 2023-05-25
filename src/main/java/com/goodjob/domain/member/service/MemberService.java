package com.goodjob.domain.member.service;

import com.goodjob.domain.member.dto.request.MemberRequestDto;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public Member join(MemberRequestDto memberRequestDto) {
        String password = passwordEncoder.encode(memberRequestDto.getPassword());

        Member member = Member
                .builder()
                .account(memberRequestDto.getAccount())
                .password(password)
                .username(memberRequestDto.getUsername())
                .email(memberRequestDto.getEmail())
                .phone(memberRequestDto.getPhone())
                .isDeleted(false)
                .build();

        return memberRepository.save(member);
    }
}
