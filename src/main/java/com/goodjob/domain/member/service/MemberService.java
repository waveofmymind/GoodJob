package com.goodjob.domain.member.service;

import com.goodjob.domain.member.dto.request.JoinRequestDto;
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
    public Member join(JoinRequestDto joinRequestDto) {
        String password = passwordEncoder.encode(joinRequestDto.getPassword());

        Member member = Member
                .builder()
                .account(joinRequestDto.getAccount())
                .password(password)
                .username(joinRequestDto.getUsername())
                .email(joinRequestDto.getEmail())
                .phone(joinRequestDto.getPhone())
                .isDeleted(false)
                .build();

        return memberRepository.save(member);
    }

    public Optional<Member> findByAccount(String account) {
        return memberRepository.findByAccount(account);
    }
}
