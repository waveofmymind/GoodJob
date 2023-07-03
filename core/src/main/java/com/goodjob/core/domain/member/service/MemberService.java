package com.goodjob.core.domain.member.service;


import com.goodjob.core.domain.member.dto.request.EditRequestDto;
import com.goodjob.core.domain.member.dto.request.JoinRequestDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.repository.MemberRepository;
import com.goodjob.core.global.base.jwt.JwtProvider;
import com.goodjob.core.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public RsData<Member> join(JoinRequestDto joinRequestDto) {
        RsData canJoinData = canJoin(joinRequestDto);

        if (canJoinData.isFail()) { // 회원가입 불가능할 경우
            return canJoinData;
        }

        String password = passwordEncoder.encode(joinRequestDto.getPassword());

        Member member = Member
                .builder()
                .username(joinRequestDto.getUsername())
                .password(password)
                .nickname(joinRequestDto.getNickname())
                .email(joinRequestDto.getEmail())
                .isDeleted(false)
                .providerType("GOODJOB")
                .userRole("free")
                .build();

        memberRepository.save(member);

        return RsData.of("S-1", "%s님의 회원가입이 완료되었습니다.".formatted(joinRequestDto.getNickname()), member);
    }

    private RsData canJoin(JoinRequestDto joinRequestDto) {
        Optional<Member> opUsername = findByUsername(joinRequestDto.getUsername());
        Optional<Member> opNickname = findByNickname(joinRequestDto.getNickname());

        if (opUsername.isPresent()) { // 로그인 계정이 중복인 경우
            return RsData.of("F-1", "이미 존재하는 계정입니다.");
        }

        if (opNickname.isPresent()) { // 닉네임이 중복인 경우
            return RsData.of("F-1", "이미 존재하는 닉네임입니다.");
        }

        return RsData.of("S-1", "회원가입이 가능합니다.");
    }

    // 일반회원가입, 소셜로그인 회원가입 나눠 처리
    @Transactional
    public RsData<Member> socialJoin(String providerType, String username, String password, String email) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
        }

        if (StringUtils.hasText(password)) {
            password = passwordEncoder.encode(password);
        }

        // 닉네임을 랜덤으로 생성
        String randomUUID = UUID.randomUUID().toString().replaceAll("-", "");
        String nickname = providerType + "__" + randomUUID.substring(0, 6); // 6자리까지만 사용

        // oauth2 로그인 이후 추가정보입력
        Member member = Member
                .builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .isDeleted(false)
                .providerType(providerType)
                .userRole("free")
                .build();

        memberRepository.save(member);

       return RsData.of("S-1", "%s님의 회원가입이 완료되었습니다.".formatted(nickname), member);
    }

    public RsData login(String username, String password) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isEmpty()) {
            return RsData.of("F-1", "아이디 혹은 비밀번호가 틀립니다.");
        }

        Member member = opMember.get();
        boolean matches = passwordEncoder.matches(password, opMember.get().getPassword());

        if (!matches) {
            return RsData.of("F-1", "아이디 혹은 비밀번호가 틀립니다.");
        }

        String accessToken = jwtProvider.genToken(member.toClaims());

        return RsData.of("S-1", "%s님 환영합니다!".formatted(member.getNickname()), accessToken);
    }

    public Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByNickName(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    // 소셜 로그인할때마다 동작
    public RsData<Member> whenSocialLogin(String providerType, String username, String email) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) {
            return RsData.of("S-1", "로그인 되었습니다.", opMember.get());
        }

        return socialJoin(providerType, username, "", email); // 최초 1회 실행
    }

    public RsData<String> matchPassword(String passwordToEdit, String memberPassword) {
        if (!passwordEncoder.matches(passwordToEdit, memberPassword)) {
            return RsData.of("F-1", "비밀번호가 일치하지않습니다.");
        }

        return RsData.of("S-1", "비밀번호가 일치합니다. 회원정보수정 페이지로 이동합니다.");
    }

    @Transactional
    public RsData update(Member member, EditRequestDto editRequestDto) {
        String nickname = editRequestDto.getNickname();
        Optional<Member> opNickName = findByNickName(nickname);

        if (nickname.length() < 2) {
            return RsData.of("F-1", "2자 이상 입력해주세요.");
        }

        if (opNickName.isPresent()) {
            return RsData.of("F-1", "이미 존재하는 닉네임 입니다.");
        }

        member.setNickname(nickname);
        memberRepository.save(member);

        return RsData.of("S-1", "회원 정보가 수정되었습니다.");
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional
    public void upgradeMembership(Member member) {
        member.setUserRole("premium");

        memberRepository.save(member);
    }

    @Transactional
    public RsData applyMentor(Member member) {
        member.setUserRole("mentor");

        memberRepository.save(member);

        return RsData.of("S-1", "멘토 등급이 되었습니다.");
    }
}

