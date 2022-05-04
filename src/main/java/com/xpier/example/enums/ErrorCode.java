package com.xpier.example.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    VALIDATION_FAILED(1000, HttpStatus.BAD_REQUEST, "Verification failed. Please try again."),
    ILLEGAL_ARGUMENTS(1001, HttpStatus.BAD_REQUEST, "Invalid variable."),
    USER_NOT_FOUND(2020, HttpStatus.NOT_FOUND, "User not found."),
    RESOURCE_NOT_FOUND(2022, HttpStatus.NOT_FOUND, "No entry found matching supplied source_lang, word and provided filters");
    private final int code;

    private final HttpStatus httpStatus;

    private final String message;

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
