package com.ssafy.aroundthekorea.security.token.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class JwtToken {
	@Id
	Long userId;

	@Column(columnDefinition = "clob")
	private String token;

	@CreatedBy
	private LocalDateTime createdAt;

	public JwtToken(Long userId, String token) {
		this.userId = userId;
		this.token = token;
	}
}
