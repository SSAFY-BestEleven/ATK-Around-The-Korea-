package com.ssafy.aroundthekorea.user.service.account;

import org.springframework.security.authentication.AuthenticationProvider;

import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationDto;
import com.ssafy.aroundthekorea.user.controller.response.LoginResponseDto;

public interface AccountService extends AuthenticationProvider {
	LoginResponseDto createTokens(JwtAuthenticationDto authenticationDto);
}
