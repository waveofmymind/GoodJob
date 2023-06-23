package com.goodjob.core.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditRequestDto {
    @NotBlank(message = "닉네임은 필수항목입니다.")
    @Size(min = 2, max = 20, message = "2자 이상 20자 이하로 입력해주세요.")
    private String nickname;
}
