package com.goodjob.member.constant;

public enum ProviderType {
    GOODJOB,
    KAKAO,
    GITHUB,
    GOOGLE;

    public static ProviderType of(String socialProviderType) {
        if (socialProviderType.equals(KAKAO.name())) {
            return KAKAO;
        } else if (socialProviderType.equals(GITHUB.name())) {
            return GITHUB;
        } else if (socialProviderType.equals(GOOGLE.name())) {
            return GOOGLE;
        }

        return GOODJOB;
    }
}
