package com.goodjob.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDto {
    private String account;
    private String password;
    private String nickname;
    private String email;
}
