package com.auctionmaster.config;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auctionmaster.exception.CustomException;
import com.auctionmaster.exception.DuplicateResourceException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<Object> duplicateResourceException(DuplicateResourceException e) {
		CustomException customException = new CustomException(
				e.getMessage(),
				HttpStatus.CONFLICT,
				LocalDateTime.now());
		return new ResponseEntity<>(customException, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> authenticationException(AuthenticationException e) {
		CustomException customException = new CustomException(
				"Invalid username or password",
				HttpStatus.BAD_REQUEST,
				LocalDateTime.now());
		return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> accessDeniedException(AccessDeniedException e) {
		CustomException customException = new CustomException(
				e.getMessage(),
				HttpStatus.FORBIDDEN,
				LocalDateTime.now());
		return new ResponseEntity<>(customException, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> defaultHandler(Exception e) {
		log.error(e.getMessage());
		CustomException customException = new CustomException(
				"Error has ocurred",
				HttpStatus.BAD_REQUEST,
				LocalDateTime.now());
		return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
	}
}
