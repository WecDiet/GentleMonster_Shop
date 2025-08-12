package com.gentlemonster.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gentlemonster.DTO.Responses.Error.ErrorResponse;

@RestControllerAdvice
public class CustomExceptionHandle {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return ErrorResponse.builder()
                .errCode(HttpStatus.NOT_FOUND.value())
                .errMessage(ex.getMessage())
                .build();
    }
}
