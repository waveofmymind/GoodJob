package com.goodjob.member.entity;


import com.goodjob.common.BaseEntity;
import com.goodjob.member.constant.Membership;
import com.goodjob.member.constant.ProviderType;
import com.goodjob.member.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
     private Long id;

    @Column(unique = true)
    private String username; // 로그인 계정

    private String password;

    @Column(unique = true)
    private String nickname; // 닉네임

    private String email;

    @Enumerated(EnumType.STRING)
    private Membership membership; // FREE, PREMIUM, MENTOR

    private int coin;

    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType; // 일반회원인지, 카카오로 가입한 회원인지, 구글로 가입한 회원인지


    // 현재 회원이 가지고 있는 권한들을 List<GrantedAuthority> 형태로 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_USER.name()));

        // membership이 mentor인 회원은 추가로 ROLE_MENTOR 권한 부여
        if (isMentor()) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_PAYED.name()));
            authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_MENTOR.name()));
        }

        // membership이 premium인 회원은 추가로 ROLE_PAYED 권한 부여
        if (isPremium()) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_PAYED.name()));
        }

        return authorities;
    }

    public Map<String, Object> toClaims() {
        return Map.of(
                "id", getId(),
                "username", getUsername(),
                "nickname", getNickname()
        );
    }

    public boolean isFree() {
        return membership.equals(Membership.FREE);
    }

    public boolean isPremium() {
        return membership.equals(Membership.PREMIUM);
    }

    public boolean isMentor() {
        return membership.equals(Membership.MENTOR);
    }

    public boolean isSocialMember() {
        return !providerType.equals(ProviderType.GOODJOB);
    }

    public void upgradeMembership(Membership targetMembership) {
        this.membership = targetMembership;
        this.coin = -1;
    }
    public void deductCoin() {
        this.coin--;
    }

    public void recoverCoin() {
        this.coin++;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
