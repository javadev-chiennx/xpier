package com.xpier.example.exception.error;

import com.xpier.example.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ApiError {

	private HttpStatus status;

	private LocalDateTime timestamp;

	private int errorCode;

	private String message;

	private List<ApiSubError> subErrors;

	private ApiError() {
		timestamp = LocalDateTime.now();
	}

	public ApiError(HttpStatus status, int errorCode) {
		this();
		this.status = status;
		this.errorCode = errorCode;
		this.message = "Unexpected error";
	}

	public ApiError(HttpStatus status, String message, int errorCode) {
		this(status, errorCode);
		this.message = message;
	}

	public ApiError(ErrorCode errorCode) {
		this(errorCode.getHttpStatus(), errorCode.getMessage(), errorCode.getCode());
	}
}
