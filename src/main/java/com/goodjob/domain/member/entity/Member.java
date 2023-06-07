package com.goodjob.domain.member.entity;

import com.goodjob.domain.BaseEntity;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.*;
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
@ToString
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; // 로그인 계정

    private String password;

    @Column(unique = true)
    private String nickname; // 닉네임

    @Column(unique = true)
    private String email;

    private String userRole; // user, admin

    private boolean isDeleted;

    private String providerType; // 일반회원인지, 카카오로 가입한 회원인지, 구글로 가입한 회원인지

    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    private List<Comment> comments = new ArrayList<>();

    // 현재 회원이 가지고 있는 권한들을 List<GrantedAuthority> 형태로 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        return authorities;
    }

    public Map<String, Object> toClaims() {
        return Map.of(
                "id", getId(),
                "username", getUsername(),
                "nickname", getNickname()
        );
    }

}
