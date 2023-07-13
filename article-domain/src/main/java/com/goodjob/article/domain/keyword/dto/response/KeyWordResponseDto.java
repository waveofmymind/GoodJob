package com.goodjob.article.domain.keyword.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyWordResponseDto {
    private Long id;
    private String content;

    public String getListUrl() {
        return "/article/list?sortCode=1&category=해시태그&query=%s".formatted(content);
    }
}
