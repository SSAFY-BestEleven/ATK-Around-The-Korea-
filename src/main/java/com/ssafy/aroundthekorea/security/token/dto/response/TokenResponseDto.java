package com.ssafy.aroundthekorea.security.token.dto.response;

import java.time.LocalDateTime;

public record TokenResponseDto(
	Long userId,
	String token,
	LocalDateTime createdAt
) {
}
