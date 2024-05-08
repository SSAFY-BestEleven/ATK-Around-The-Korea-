package com.ssafy.aroundthekorea.security.token.service;

import static java.text.MessageFormat.format;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.aroundthekorea.security.token.domain.JwtToken;
import com.ssafy.aroundthekorea.security.token.repository.JwtTokenRepository;
import com.ssafy.aroundthekorea.security.token.dto.response.TokenResponseDto;
import com.ssafy.aroundthekorea.exception.model.NotFoundResource;

@Transactional(readOnly = true)
@Service
public class TokenService {
	private final JwtTokenRepository repository;

	public TokenService(JwtTokenRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public void deleteExpiredToken() {
		repository.deleteExpiredToken(LocalDateTime.now());
	}

	public TokenResponseDto getToken(Long userId) {
		JwtToken jwtToken = repository.findById(userId)
			.orElseThrow(
				() -> new NotFoundResource(format(("사용자  userId -> [{0}] 은 토큰을 발급받지 않거나 혹은 만료된 토큰입니다."), userId)));

		return new TokenResponseDto(
			jwtToken.getUserId(),
			jwtToken.getToken(),
			jwtToken.getCreatedAt()
		);
	}
}
