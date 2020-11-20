package com.pay.money.throwing.error;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private String code;

    private String description;

    private String detail;

    public ErrorResponse(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public ErrorResponse(String code, String description, String detail) {
        this.code = code;
        this.description = description;
        this.detail = detail;
    }

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.description = errorCode.getMessage();
        this.detail = "";
    }
}
