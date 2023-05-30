package com.goodjob.domain.member.dto.request;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String account;
    private String password;
}
