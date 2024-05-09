package com.ssafy.aroundthekorea.security.jwt;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ssafy.aroundthekorea.user.domain.User;

@SpringBootTest
class JwtHandlerTest {
	@Autowired
	JwtTokenProperties jwtTokenProperties;

	@Autowired
	JwtHandler jwtHandler;

	User user;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.id(1L)
			.username("docker123")
			.email("docker@google.com")
			.password("{encrypt} dodockerizer1784!")
			.build();
	}

	@DisplayName("토큰 검증에 성공한다.")
	@Test
	void testVerifiedToken() {
		//given
		var givenAccessClaims = JwtHandler.Claims.of(user.getId(), user.getUsername(), new String[] {"ROLE_USER"});
		var givenRefreshClaims = JwtHandler.Claims.of(user.getId());

		String accessToken = jwtHandler.createForAccess(givenAccessClaims);
		String refreshToken = jwtHandler.createForAccess(givenRefreshClaims);
		//when
		JwtHandler.Claims verifiedAccessToken = jwtHandler.verify(accessToken);
		JwtHandler.Claims verifiedRefreshToken = jwtHandler.verify(refreshToken);
		//then
		assertThat(verifiedAccessToken.getUserId()).isEqualTo(givenAccessClaims.getUserId());
		assertThat(verifiedAccessToken.getUsername()).isEqualTo(givenAccessClaims.getUsername());
		assertThat(verifiedAccessToken.getRoles()).isEqualTo(new String[] {"ROLE_USER"});
		assertThat(verifiedRefreshToken.getUserId()).isEqualTo(givenRefreshClaims.getUserId());
	}

	@DisplayName("토큰이 위조되면 예외가 발생한다.")
	@Test
	void failVerifiedToken() {
		//given
		var givenClaims = JwtHandler.Claims.of(user.getId(), user.getUsername(), new String[] {"ROLE_USER"});
		String invalidToken = jwtHandler.createForAccess(givenClaims) + "select * from users";
		//when
		//then
		Assertions.assertThatThrownBy(() -> jwtHandler.verify(invalidToken))
			.isInstanceOf(JWTVerificationException.class);
	}

	@DisplayName("엑세스 토큰이 만료되면 예외가 발생한다.")
	@Test
	void failExpiredAccessToken() throws InterruptedException {
		//given
		var givenClaims = JwtHandler.Claims.of(user.getId(), user.getUsername(), new String[] {"ROLE_USER"});
		String accessToken = jwtHandler.createForAccess(givenClaims);
		long sleepTime = jwtTokenProperties.accessExpirySeconds() * 2000L;
		Thread.sleep(sleepTime);
		//when
		//then
		assertThatThrownBy(() -> jwtHandler.verify(accessToken))
			.isInstanceOf(TokenExpiredException.class);
	}

	@DisplayName("리프레시 토큰이 만료되면 예외가 발생한다.")
	@Test
	void failExpiredRefreshToken() throws InterruptedException {
		//given
		var givenClaims = JwtHandler.Claims.of(user.getId());
		String refreshToken = jwtHandler.createForRefresh(givenClaims);
		long sleepTime = jwtTokenProperties.refreshExpirySeconds() * 1500L;
		Thread.sleep(sleepTime);
		//when
		//then
		assertThatThrownBy(() -> jwtHandler.verify(refreshToken))
			.isInstanceOf(TokenExpiredException.class);
	}
}