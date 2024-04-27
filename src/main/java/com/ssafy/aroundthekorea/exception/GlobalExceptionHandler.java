package com.ssafy.aroundthekorea.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handle(RuntimeException e) {
		log.info("[서버 오류] -> {}", e.getMessage());
		return ResponseEntity.internalServerError().body("관리자에게 문의해주세요.");
	}

	@ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
	public ResponseEntity<?> handle(BindException e) {
		log.info("[입력 예외] -> {}", e.getMessage());
		return ResponseEntity.badRequest().body("입력 오류가 발생했습니다.");
	}
}
