package com.auctionmaster.config;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auctionmaster.exception.CustomException;
import com.auctionmaster.exception.DuplicateResourceException;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<Object> duplicateResourceException(DuplicateResourceException e) {
		CustomException customException = new CustomException(
			e.getMessage(),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		);
		return new ResponseEntity<>(customException, HttpStatus.CONFLICT);
	}
}
