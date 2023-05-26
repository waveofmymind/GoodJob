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

    public boolean canJoin(JoinRequestDto joinRequestDto) {
        findByNickname(joinRequestDto.getNickname());
        Optional<Member> opAccount = findByAccount(joinRequestDto.getAccount());
        Optional<Member> opEmail = findByEmail(joinRequestDto.getEmail());

        if (opAccount.isPresent()) { // 로그인 계정이 중복인 경우
            return false;
        }

        if (opEmail.isPresent()) { // 이메일이 중복인 경우
            return false;
        }

        return true;
    }

    @Transactional
    public Member join(JoinRequestDto joinRequestDto) {
        String password = passwordEncoder.encode(joinRequestDto.getPassword());

        Member member = Member
                .builder()
                .account(joinRequestDto.getAccount())
                .password(password)
                .nickname(joinRequestDto.getNickname())
                .email(joinRequestDto.getEmail())
                .isDeleted(false)
                .build();

        return memberRepository.save(member);
    }

    private Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findByAccount(String account) {
        return memberRepository.findByAccount(account);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }
}
