package com.ssafy.aroundthekorea.security.jwt.dto;

public class JwtAuthentication {
	private Long userId;
	private Long username;

	public JwtAuthentication(Long userId, Long username) {
		this.userId = userId;
		this.username = username;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getUsername() {
		return username;
	}
}
