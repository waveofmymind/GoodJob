package com.goodjob.resume.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Contents {

    @OneToMany(fetch = FetchType.EAGER,orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Content> contents = new ArrayList<>();

    public static Contents of(List<String> answers) {
        Contents instance = new Contents();
        answers.forEach(answer -> instance.contents.add(Content.of(answer)));
        return instance;
    }
}
