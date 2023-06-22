package com.goodjob.core.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class LoginRequestDto {
    @Size(min = 4, max = 30)
    @NotBlank(message = "아이디는 필수항목입니다.")
    private String username;

    @Size(min = 4, max = 15)
    @NotBlank(message = "비밀번호는 필수항목입니다.")
    private String password;
}
