package com.goodjob.domain.resume.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Answers {
    @ElementCollection
    private List<String> answers;

    protected Answers() {
        this.answers = new ArrayList<>();
    }

    public Answers(List<String> questions) {
        this.answers = new ArrayList<>(questions);
    }

    public List<String> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

    public static Answers of(List<String> answers) {
        Answers instance = new Answers();
        instance.answers.addAll(answers);
        return instance;
    }
}