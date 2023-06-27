package com.goodjob.core.domain.resume.domain;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Titles {
    @ElementCollection
    @Column(columnDefinition = "TEXT")
    private List<String> titles;

    protected Titles() {
        this.titles = new ArrayList<>();
    }

    public Titles(List<String> questions) {
        this.titles = new ArrayList<>(questions);
    }

    public List<String> getQuestions() {
        return Collections.unmodifiableList(titles);
    }

    public static Titles of(List<String> questions) {
        Titles instance = new Titles();
        instance.titles.addAll(questions);
        return instance;
    }
}
