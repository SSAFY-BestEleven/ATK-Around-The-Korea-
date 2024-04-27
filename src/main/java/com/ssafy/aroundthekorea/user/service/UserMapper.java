package com.ssafy.aroundthekorea.user.service;

import org.springframework.stereotype.Component;

import com.ssafy.aroundthekorea.user.controller.request.SignUpUserRequestDto;
import com.ssafy.aroundthekorea.user.domain.User;

@Component
public class UserMapper {
	public User toUser(SignUpUserRequestDto requestDto) {
		return User.builder()
			.username(requestDto.username())
			// todo: encryption
			.passowrd(requestDto.password())
			.email(requestDto.email())
			.build();
	}

}
