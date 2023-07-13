package com.goodjob.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EditRequestDto {
    @NotBlank(message = "닉네임은 필수항목입니다.")
    @Pattern(regexp = "\\S+", message = "닉네임에 공백은 사용할 수 없습니다.")
    @Size(min = 2, max = 20, message = "2자 이상 20자 이하로 입력해주세요.")
    private String nickname;

    @Size(min = 4, max = 15, message = "4자 이상 15자 이하로 입력해주세요.")
    private String password;
}
