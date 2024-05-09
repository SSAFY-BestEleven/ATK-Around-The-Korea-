package com.ssafy.aroundthekorea.user.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationDto;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationToken;
import com.ssafy.aroundthekorea.user.controller.request.LoginRequestDto;
import com.ssafy.aroundthekorea.user.controller.request.SignUpUserRequestDto;
import com.ssafy.aroundthekorea.user.controller.response.LoginResponseDto;
import com.ssafy.aroundthekorea.user.service.account.AccountService;
import com.ssafy.aroundthekorea.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@RestController
public class AccountController {
	private final UserService userService;
	private final AccountService accountService;

	@PostMapping
	public void signUp(@Validated @RequestBody SignUpUserRequestDto requestDto) {
		userService.create(requestDto);
	}

	@PostMapping(path = "/login")
	public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDto authRequest) {
		var jwtAuthenticationToken = new JwtAuthenticationToken(authRequest.username(), authRequest.password());
		Authentication authenticatedAuth = accountService.authenticate(jwtAuthenticationToken);
		JwtAuthenticationDto details = (JwtAuthenticationDto)authenticatedAuth.getDetails();
		LoginResponseDto responseDto = accountService.createTokens(details);

		return new ResponseEntity<>(responseDto, new HttpHeaders(), HttpStatus.OK);
	}

}
