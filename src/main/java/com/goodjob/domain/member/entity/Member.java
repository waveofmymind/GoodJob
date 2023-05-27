package com.goodjob.domain.member.entity;

import com.goodjob.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    private String account;

    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    private String userRole; // user, admin

    private boolean isDeleted;
}
