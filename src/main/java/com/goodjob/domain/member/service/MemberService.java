package com.goodjob.domain.member.service;

import com.goodjob.domain.member.dto.request.JoinRequestDto;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.repository.MemberRepository;
import com.goodjob.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    private RsData canJoin(JoinRequestDto joinRequestDto) {
        log.info("joinRequestDto= {}", joinRequestDto.toString());

        Optional<Member> opNickname = findByNickname(joinRequestDto.getNickname());
        Optional<Member> opAccount = findByAccount(joinRequestDto.getAccount());
        Optional<Member> opEmail = findByEmail(joinRequestDto.getEmail());

        if (opNickname.isPresent()) { // 닉네임이 중복인 경우
            return RsData.of("F-1", "이미 존재하는 닉네임입니다.");
        }

        if (opAccount.isPresent()) { // 로그인 계정이 중복인 경우
            return RsData.of("F-1", "이미 존재하는 계정입니다.");
        }

        if (opEmail.isPresent()) { // 이메일이 중복인 경우
            return RsData.of("F-1", "이미 존재하는 이메일입니다.");
        }

        return RsData.of("S-1", "회원가입이 가능합니다.");
    }

    @Transactional
    public RsData<Member> join(JoinRequestDto joinRequestDto) {
        RsData canJoinData = canJoin(joinRequestDto);

        if (canJoinData.isFail()) { // 회원가입 불가능할 경우
            return canJoinData;
        }

        String password = passwordEncoder.encode(joinRequestDto.getPassword());

        Member member = Member
                .builder()
                .account(joinRequestDto.getAccount())
                .password(password)
                .nickname(joinRequestDto.getNickname())
                .email(joinRequestDto.getEmail())
                .isDeleted(false)
                .build();

        memberRepository.save(member);

        return RsData.of("S-1", "%s님의 회원가입이 완료되었습니다.".formatted(joinRequestDto.getNickname()), member);
    }

    public RsData canLogin(String account, String password) {
        Member member = findByAccount(account).orElse(null);
        log.info("member ={}", member.toString());

        if (member == null) { // 계정이 존재하지 않는 경우
            return RsData.of("F-1", "아이디 혹은 비밀번호가 틀립니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        if (!encodedPassword.equals(member.getPassword())) { // 비밀번호가 다른 경우
            return RsData.of("F-1", "아이디 혹은 비밀번호가 틀립니다.");
        }

        return RsData.of("S-1", "로그인 가능합니다.");
    }

    private Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    private Optional<Member> findByAccount(String account) {
        return memberRepository.findByAccount(account);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }
}
