package com.goodjob.domain.resume.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Questions {
    @ElementCollection
    private List<String> questions;

    protected Questions() {
        this.questions = new ArrayList<>();
    }

    public Questions(List<String> questions) {
        this.questions = new ArrayList<>(questions);
    }

    public List<String> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    public static Questions of(List<String> questions) {
        Questions instance = new Questions();
        instance.questions.addAll(questions);
        return instance;
    }
}
