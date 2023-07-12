package com.goodjob.common.cookie.constant;

public enum CookieType {
    ACCESS_TOKEN("accessToken"),
    REFRESH_TOKEN("refreshToken"),
    PREVIOUS_URL("previousUrl"),
    EMAIL("email");

    private final String value;

    CookieType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
