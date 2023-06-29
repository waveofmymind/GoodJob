package com.goodjob.core.domain.resume.domain;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Contents {
    @ElementCollection
    @Column(columnDefinition = "TEXT")
    private List<String> contents;

    protected Contents() {
        this.contents = new ArrayList<>();
    }

    public Contents(List<String> questions) {
        this.contents = new ArrayList<>(questions);
    }

    public List<String> getContents() {
        return Collections.unmodifiableList(contents);
    }

    public static Contents of(List<String> answers) {
        Contents instance = new Contents();
        instance.contents.addAll(answers);
        return instance;
    }
}
