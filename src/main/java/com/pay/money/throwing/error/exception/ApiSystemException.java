package com.pay.money.throwing.error.exception;

import com.pay.money.throwing.error.ErrorCode;

public class ApiSystemException extends RuntimeException {

    private final ErrorCode errorCode;

    public ApiSystemException(final ErrorCode errorCode) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
