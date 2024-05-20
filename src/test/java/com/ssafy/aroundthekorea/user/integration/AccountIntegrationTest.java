package com.ssafy.aroundthekorea.user.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.aroundthekorea.security.jwt.JwtHandler;
import com.ssafy.aroundthekorea.security.jwt.JwtTokenProperties;
import com.ssafy.aroundthekorea.security.token.domain.JwtToken;
import com.ssafy.aroundthekorea.security.token.repository.JwtTokenRepository;
import com.ssafy.aroundthekorea.user.controller.request.LoginRequestDto;
import com.ssafy.aroundthekorea.user.controller.request.SignUpUserRequestDto;
import com.ssafy.aroundthekorea.user.controller.response.LoginResponseDto;
import com.ssafy.aroundthekorea.user.domain.User;
import com.ssafy.aroundthekorea.user.domain.repository.UserRepository;

import io.restassured.http.Header;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountIntegrationTest {

	@LocalServerPort
	int port;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProperties jwtTokenProperties;

	@Autowired
	JwtHandler jwtHandler;

	@Autowired
	JwtTokenRepository tokenRepository;

	@DisplayName("회원가입에 성공한다.")
	@Test
	void testSignUp() {
		var request = new SignUpUserRequestDto("docker123",
			"{encryption} dn1!323nj@",
			"docweqe123@gmail.com");

		given().port(port).body(request).contentType(JSON).log().all().
			when().post("/api/v1/accounts")
			.then().log().all()
			.statusCode(HttpStatus.SC_OK);
	}

	@DisplayName("로그인을 화면 토큰 두개를 반환한다.")
	@Test
	void testLogin() {
		//given
		String rawPassword = "dsmkao1239!";
		User user = User.builder()
			.username("docker1784")
			.password(passwordEncoder.encode(rawPassword))
			.email("DOCK133@gmail.com")
			.build();
		//when
		//then
		User requestLoginUser = userRepository.save(user);
		LoginRequestDto request = new LoginRequestDto(user.getUsername(), rawPassword);
		LoginResponseDto responseDto = given().port(port).body(request).contentType(JSON).log().all().
			when().post("/api/v1/accounts/login")
			.then().log().all()
			.statusCode(HttpStatus.SC_OK)
			.body("access.headerName", equalTo(jwtTokenProperties.accessHeader()))
			.body("refresh.headerName", equalTo(jwtTokenProperties.refreshHeader()))
			.extract().body().as(LoginResponseDto.class);

		String accessToken = responseDto.access().token();
		JwtHandler.Claims verifiedAccessClaim = jwtHandler.verify(accessToken);
		String refreshToken = responseDto.access().token();
		JwtHandler.Claims verifiedRefreshClaim = jwtHandler.verify(refreshToken);
		assertThat(verifiedAccessClaim.getUserId()).isEqualTo(requestLoginUser.getId());
		assertThat(verifiedAccessClaim.getUsername()).isEqualTo(requestLoginUser.getUsername());
		assertThat(verifiedAccessClaim.getRoles()).isEqualTo(new String[] {"ROLE_USER"});
		assertThat(verifiedRefreshClaim.getUserId()).isEqualTo(requestLoginUser.getId());
	}

	@DisplayName("인증되지 않는 사용자는 로그아웃을 할 수 없다")
	@Test
	void testLogoutByAuthenticationUser() {
		//given
		//when
		//then
		given().port(port).contentType(JSON).log().all().
			when().delete("/api/v1/accounts/logout")
			.then().log().all()
			.statusCode(HttpStatus.SC_UNAUTHORIZED);
	}

	@DisplayName("인증된 사용자는 리프레시 토큰을 제거한다.")
	@Test
	void failLogoutByNotAuthenticationUser() {
		//given
		User savedUser = userRepository.save(User.builder()
			.username("docker0102r")
			.email("dock@gmai.com")
			.password("{encrypt} password")
			.build());

		String accessToken = jwtHandler.createForAccess(
			JwtHandler.Claims.of(savedUser.getId(), savedUser.getUsername(), new String[] {"ROLE_USER"}));
		String refreshToken = jwtHandler.createForRefresh(JwtHandler.Claims.of(savedUser.getId()));
		tokenRepository.save(new JwtToken(savedUser.getId(), refreshToken));
		//when
		//then
		given().port(port)
			.header(new Header(jwtTokenProperties.accessHeader(), accessToken))
			.header(new Header(jwtTokenProperties.refreshHeader(), refreshToken))
			.contentType(JSON)
			.log().all().
			when().delete("/api/v1/accounts/logout")
			.then().log().all()
			.statusCode(HttpStatus.SC_OK);
	}
}
