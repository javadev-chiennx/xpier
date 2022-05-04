package com.xpier.example.exception.handler;

import com.xpier.example.enums.ErrorCode;
import com.xpier.example.exception.BusinessException;
import com.xpier.example.exception.error.ApiError;
import com.xpier.example.exception.error.ApiSubError;
import com.xpier.example.exception.error.ApiValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
        log.error(ex.getMessage());
        var apiErr = new ApiError(ex.getStatus(), ex.getMessage(), ex.getErrorCode().getCode());
        return new ResponseEntity<>(apiErr, apiErr.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        var apiError = new ApiError(ErrorCode.VALIDATION_FAILED);
        List<ApiSubError> validationErrors =
                ex.getBindingResult().getFieldErrors().stream().map(objectError -> new ApiValidationError(objectError.getField(), objectError.getDefaultMessage())).collect(Collectors.toList());
        apiError.setSubErrors(validationErrors);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(value = {WebClientResponseException.class})
    protected ResponseEntity<Object> handleWebClientResponse(WebClientResponseException ex) {
        var apiErr = new ApiError(ex.getStatusCode(), ex.getMessage(), ex.getRawStatusCode());
        return new ResponseEntity<>(apiErr, apiErr.getStatus());
    }
}
