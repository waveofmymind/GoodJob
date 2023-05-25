package com.goodjob.domain.member.dto.request;

import lombok.Getter;

@Getter
public class MemberRequestDto {
    private String account;
    private String password;
    private String username;
    private String email;
    private String phone;
}
