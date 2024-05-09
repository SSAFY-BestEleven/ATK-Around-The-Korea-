package com.ssafy.aroundthekorea.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ssafy.aroundthekorea.exception.model.BusinessException;

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
		return ResponseEntity.badRequest()
			.body("입력 오류가 발생했습니다.");
	}

	@ExceptionHandler({JWTVerificationException.class})
	public ResponseEntity<?> handle(JWTVerificationException e) {
		log.info(e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body("인증 오류가 발생하였습니다.");
	}

	@ExceptionHandler(JWTCreationException.class)
	public ResponseEntity<?> handle(JWTCreationException e) {
		log.info(e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body("로그인이 필요합니다.");
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handle(AuthenticationException e) {
		log.info(e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body("로그인이 필요합니다.");
	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handle(BusinessException e) {
		log.info(e.getMessage());
		return ResponseEntity.badRequest()
			.body(e.getClientMessage());
	}
}
