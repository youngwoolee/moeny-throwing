package com.pay.money.throwing.error.exception;

import com.pay.money.throwing.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiValidationExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse processValidationError(MethodArgumentNotValidException e) {
        return makeErrorResponse(e.getBindingResult());
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult){
        String code = String.valueOf(HttpStatus.BAD_REQUEST.value());
        String description = "";
        String detail = "";

        if(bindingResult.hasErrors()){
            detail = bindingResult.getFieldError().getDefaultMessage();
            StringBuilder builder = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                builder.append("[");
                builder.append(fieldError.getField());
                builder.append("](은)는 ");
                builder.append(fieldError.getDefaultMessage());
                description = builder.toString();

            }
        }
        return new ErrorResponse(code, description, detail);
    }

}
