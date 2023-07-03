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
public class Titles {
    @OneToMany(fetch = FetchType.EAGER,orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Title> titles = new ArrayList<>();

    public static Titles of(List<String> questions) {
        Titles instance = new Titles();
        questions.forEach(question -> instance.titles.add(Title.of(question)));
        return instance;
    }
}
