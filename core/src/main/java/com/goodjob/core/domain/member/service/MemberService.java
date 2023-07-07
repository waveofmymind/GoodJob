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

import static com.goodjob.core.global.base.coin.CoinUt.MAX_COIN_COUNT;

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
        String nickname = joinRequestDto.getNickname().replaceAll("\\s+", "");

        Member member = Member
                .builder()
                .username(joinRequestDto.getUsername())
                .password(password)
                .nickname(nickname)
                .email(joinRequestDto.getEmail())
                .isDeleted(false)
                .providerType("GOODJOB")
                .userRole("free")
                .coin(MAX_COIN_COUNT)
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
                .coin(MAX_COIN_COUNT)
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
        String nickname = editRequestDto.getNickname().replaceAll("\\s+", "");
        RsData nicknameVerificationRsData = verifyProvidedNickname(member.getNickname(), nickname);

        if (nicknameVerificationRsData.isFail()) {
            return nicknameVerificationRsData;
        }

        if (!member.isSocialMember()) {
            boolean matches = passwordEncoder.matches(editRequestDto.getPassword(), member.getPassword());

            // 기존 회원정보와 변화 없는 경우
            if (member.getNickname().equals(nickname) && matches) {
                return RsData.of("F-1", "수정된 정보가 없습니다!");
            }

            String password = passwordEncoder.encode(editRequestDto.getPassword());

            member.setPassword(password);
        }

        member.setNickname(nickname);
        memberRepository.save(member);

        return RsData.of("S-1", "회원 정보가 수정되었습니다.");
    }

    private RsData verifyProvidedNickname(String originalNickname, String providedNickname) {
        Optional<Member> opNickName = findByNickname(providedNickname);

        // 바꾸려는 닉네임과 현재 회원의 닉네임이 같은 경우 변경없음
        if (opNickName.isPresent() && !originalNickname.equals(providedNickname)) {
            return RsData.of("F-1", "이미 존재하는 닉네임 입니다.");
        }

        return RsData.of("S-1", "수정 가능한 닉네임 입니다.");
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional
    public void upgradeMembership(Member member) {
        member.upgradeMembership("premium");

        memberRepository.save(member);
    }

    @Transactional
    public RsData applyMentor(Member member) {
        member.upgradeMembership("mentor");

        memberRepository.save(member);

        return RsData.of("S-1", "멘토 등급이 되었습니다.");
    }

    @Transactional
    public void deductCoin(Member member) {
        member.deductCoin();

        memberRepository.save(member);
    }

    @Transactional
    public void updateCoins() {
        memberRepository.updateCoinForFreeMembers(MAX_COIN_COUNT);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public EditRequestDto genEditRequestDtoWithTempPassword(Member member) {
        // 임시비밀번호 생성 후 저장
        String tempPassword = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15);
        EditRequestDto editRequestDto = EditRequestDto.builder()
                .nickname(member.getNickname())
                .password(tempPassword)
                .build();

        return editRequestDto;
    }
}