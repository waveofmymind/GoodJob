package com.goodjob.api.controller.resume;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
@Builder
public class FindPredictionFeignResponse {

    private Long id;
    private List<Title> titles;
    private List<Content> contents;
    private String serviceType;
    private LocalDateTime createdDate;

    @Getter
    @Setter

    public static class Title {
        private String title;

        // getters and setters
    }
    @Getter
    @Setter
    public static class Content {
        private String content;

        // getters and setters
    }
}