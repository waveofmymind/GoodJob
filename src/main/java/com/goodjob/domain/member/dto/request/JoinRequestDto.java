package com.goodjob.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDto {
    @NotBlank
    @Size(min = 4, max = 30)
    private String account;
    @NotBlank
    @Size(min = 4, max = 15)
    private String password;
    @NotBlank
    @Size(min = 2, max = 20)
    private String nickname;
    @NotBlank
    @Email
    private String email;
}
