package com.goodjob.member.constant;

public enum Membership {
    FREE("free"),
    PREMIUM("premium"),
    MENTOR("mentor");

    private final String value;

    Membership(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
