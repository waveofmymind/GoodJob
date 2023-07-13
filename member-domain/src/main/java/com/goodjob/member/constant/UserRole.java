package com.goodjob.member.constant;

public enum UserRole {

    ROLE_USER,
    ROLE_PAYED,
    ROLE_MENTOR;

    @Override
    public String toString() {
        return name();
    }
}