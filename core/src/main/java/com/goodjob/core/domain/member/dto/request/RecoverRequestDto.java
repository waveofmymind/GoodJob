package com.goodjob.core.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoverRequestDto {
    @NotBlank(message = "아이디는 필수항목입니다.")
    @Size(min = 4, max = 30, message = "4자 이상 30자 이하로 입력해주세요.")
    private String username;

    @NotBlank(message = "이메일은 필수항목입니다.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;
}