package com.goodjob.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String account;
    private String password;
}
