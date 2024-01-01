package com.auctionmaster.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class CustomException {
	private final String message;
	private final HttpStatus httpStatus;
	private final LocalDateTime timestamp;
}
