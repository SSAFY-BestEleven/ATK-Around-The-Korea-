package com.ssafy.aroundthekorea.user.service.mapper;

import org.springframework.stereotype.Component;

import com.ssafy.aroundthekorea.user.controller.request.SignUpUserRequestDto;
import com.ssafy.aroundthekorea.user.domain.User;

@Component
public class UserMapper {
	public User toUser(SignUpUserRequestDto requestDto) {
		return User.builder()
			.username(requestDto.username())
			.password(requestDto.password())
			.email(requestDto.email())
			.build();
	}

}
