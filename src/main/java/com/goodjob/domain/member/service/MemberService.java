package com.goodjob.domain.member.service;

import com.goodjob.domain.member.dto.request.JoinRequestDto;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.repository.MemberRepository;
import com.goodjob.global.base.jwt.JwtProvider;
import com.goodjob.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    private RsData canJoin(JoinRequestDto joinRequestDto) {
        Optional<Member> opUsername = findByUsername(joinRequestDto.getUsername());
        Optional<Member> opNickname = findByNickname(joinRequestDto.getNickname());
        Optional<Member> opEmail = findByEmail(joinRequestDto.getEmail());

        if (opUsername.isPresent()) { // 로그인 계정이 중복인 경우
            return RsData.of("F-1", "이미 존재하는 계정입니다.");
        }

        if (opNickname.isPresent()) { // 닉네임이 중복인 경우
            return RsData.of("F-1", "이미 존재하는 닉네임입니다.");
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
                .username(joinRequestDto.getUsername())
                .password(password)
                .nickname(joinRequestDto.getNickname())
                .email(joinRequestDto.getEmail())
                .isDeleted(false)
                .build();

        memberRepository.save(member);

        return RsData.of("S-1", "%s님의 회원가입이 완료되었습니다.".formatted(joinRequestDto.getNickname()), member);
    }

    public RsData genAccessToken(String username, String password) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isEmpty()) {
            return RsData.of("F-1", "아이디 혹은 비밀번호가 틀립니다.");
        }

        Member member = opMember.get();

        boolean matches = passwordEncoder.matches(password, member.getPassword());

        if (!matches) {
            return RsData.of("F-1", "아이디 혹은 비밀번호가 틀립니다.");
        }

        String accessToken = jwtProvider.genToken(member.toClaims());

        return RsData.of("S-1", "로그인 가능합니다.", accessToken);
    }

    private Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByNickName(String nickname) {
        return memberRepository.findByNickname(nickname);
    }
}
