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

    @ExceptionHandler(RateLimitExceededException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse handleRateLimitExceededException(RateLimitExceededException ex) {
        return ErrorResponse.builder()
                .errCode(HttpStatus.TOO_MANY_REQUESTS.value())
                .errMessage(ex.getMessage())
                .build();
    }

    // Optional: Handler cho các RuntimeException khác để tránh che lấp lỗi
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralRuntimeException(RuntimeException ex) {
        return ErrorResponse.builder()
                .errCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errMessage("Internal server error: " + ex.getMessage())
                .build();
    }
}
