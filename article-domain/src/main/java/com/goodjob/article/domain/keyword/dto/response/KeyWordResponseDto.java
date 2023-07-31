package com.goodjob.article.domain.keyword.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class KeyWordResponseDto implements Serializable {
    private Long id;
    private String content;

    public String getListUrl() {
        return "/article/list?sortCode=1&category=해시태그&query=%s".formatted(content);
    }
}
