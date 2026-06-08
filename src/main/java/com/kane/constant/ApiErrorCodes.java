package com.kane.constant;

import lombok.Getter;

@Getter
public enum ApiErrorCodes implements Error {
    INVALID_INPUT(1001, "Invalid request input"),
    NOT_FOUND(1002, "Resource not found"),
    ERROR_WHILE_SENDING_EMAIL(10008, "Error while sending email"),
    INVALID_EMAIL_CODE(10009, "Invalid email code"),
    INVALID_USERNAME_OR_PASSWORD(10006, "Invalid username or password"),
    FILE_PARSING_ERROR(10010, "File parsing error"),
    USER_NOT_FOUND(10005, "user not found");


    private int errorCode;
    private String errorMessage;

    ApiErrorCodes(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}

