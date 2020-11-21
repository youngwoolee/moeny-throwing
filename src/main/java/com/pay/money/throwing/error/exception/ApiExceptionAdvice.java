package com.pay.money.throwing.error.exception;

import com.pay.money.throwing.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({ApiSystemException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse apiExceptionHandler(HttpServletRequest request, HttpServletResponse response, ApiSystemException ex) {
        log.error("ApiException. URL : '{}', errorCode : {}, errorMessage : {}", request.getRequestURL(), ex.getErrorCode(), ex.getErrorCode().getMessage());
        return new ErrorResponse(ex.getErrorCode());
    }

}
