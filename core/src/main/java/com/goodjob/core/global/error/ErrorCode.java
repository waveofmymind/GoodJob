package com.goodjob.core.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    CREATE_PREDICTION_QUESTION(HttpStatus.BAD_REQUEST,"예상 질문을 만들 수 없습니다."),
    THREAD_MALFUNCTION(HttpStatus.INTERNAL_SERVER_ERROR,  "쓰레드 충돌"),
    PROMPT_NOT_EXISTS(HttpStatus.NOT_FOUND, "존재하지 않는 프롬프트입니다."),
    JSON_PARSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱에 실패했습니다."),

    PREDICTION_NOT_FOUND(HttpStatus.NOT_FOUND, "AI 응답 정보가 존재하지 않습니다.");


    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;
}
