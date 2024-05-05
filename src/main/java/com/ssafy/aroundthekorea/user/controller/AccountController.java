package com.ssafy.aroundthekorea.user.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.aroundthekorea.user.controller.request.SignUpUserRequestDto;
import com.ssafy.aroundthekorea.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@RestController
public class AccountController {
	private final UserService userService;

	@PostMapping
	public void signUp(@Validated @RequestBody SignUpUserRequestDto requestDto) {
		userService.create(requestDto);
	}

}
