package com.ssafy.aroundthekorea.security.jwt.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationDto;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationToken;

public class MockUserCustomFactory implements WithSecurityContextFactory<WithJwtMockUser> {
	@Override
	public SecurityContext createSecurityContext(WithJwtMockUser withMockUser) {
		JwtAuthenticationDto principal = new JwtAuthenticationDto(withMockUser.id(), withMockUser.username());
		List<SimpleGrantedAuthority> authorities = getAuthorities(withMockUser);
		var jwtAuthenticationToken = new JwtAuthenticationToken(principal,null);

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(jwtAuthenticationToken);

		return context;
	}

	private List<SimpleGrantedAuthority> getAuthorities(WithJwtMockUser withMockUser) {
		return Arrays.stream(withMockUser.role())
			.map(SimpleGrantedAuthority::new)
			.toList();
	}
}
