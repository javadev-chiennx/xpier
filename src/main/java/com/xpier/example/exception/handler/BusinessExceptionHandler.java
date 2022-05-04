package com.xpier.example.exception.handler;

import com.xpier.example.exception.BusinessException;
import com.xpier.example.exception.error.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
@Slf4j
public class BusinessExceptionHandler {

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
        log.error(ex.getMessage());
        var apiErr = new ApiError(ex.getStatus(), ex.getMessage(), ex.getErrorCode().getCode());
        return new ResponseEntity<>(apiErr, apiErr.getStatus());
    }

    @ExceptionHandler(value = {WebClientResponseException.class})
    protected ResponseEntity<Object> handleWebClientResponse(WebClientResponseException ex) {
        var apiErr = new ApiError(ex.getStatusCode(), ex.getMessage(), ex.getRawStatusCode());
        return new ResponseEntity<>(apiErr, apiErr.getStatus());
    }
}
