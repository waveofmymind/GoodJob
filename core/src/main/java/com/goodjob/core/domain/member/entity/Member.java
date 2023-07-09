package com.goodjob.core.domain.member.entity;


import com.goodjob.core.domain.BaseEntity;
import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.comment.entity.Comment;
import com.goodjob.core.domain.member.constant.Membership;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.goodjob.core.domain.member.constant.Membership.*;
import static com.goodjob.core.domain.member.constant.UserRole.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
     private Long id;

    @Column(unique = true)
    private String username; // 로그인 계정

    @Setter
    private String password;

    @Column(unique = true)
    @Setter
    private String nickname; // 닉네임

    private String email;

    private Membership membership; // FREE, PREMIUM, MENTOR

    private int coin;

    private boolean isDeleted;

    private String providerType; // 일반회원인지, 카카오로 가입한 회원인지, 구글로 가입한 회원인지

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    // 현재 회원이 가지고 있는 권한들을 List<GrantedAuthority> 형태로 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(ROLE_USER.name()));

        // membership이 mentor인 회원은 추가로 ROLE_MENTOR 권한 부여
        if (isMentor()) {
            authorities.add(new SimpleGrantedAuthority(ROLE_PAYED.name()));
            authorities.add(new SimpleGrantedAuthority(ROLE_MENTOR.name()));
        }

        // membership이 premium인 회원은 추가로 ROLE_PAYED 권한 부여
        if (isPremium()) {
            authorities.add(new SimpleGrantedAuthority(ROLE_PAYED.name()));
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
        return membership.equals(FREE);
    }

    public boolean isPremium() {
        return membership.equals(PREMIUM);
    }

    public boolean isMentor() {
        return membership.equals(MENTOR);
    }

    public boolean isSocialMember() {
        if (providerType.equals("GOODJOB")) {
            return false;
        }

        return true;
    }

    public void upgradeMembership(Membership targetMembership) {
        this.membership = targetMembership;
        this.coin = -1;
    }

    public void deductCoin() {
        this.coin--;
    }
}
