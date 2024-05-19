package com.ssafy.aroundthekorea.security.interceptor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.aroundthekorea.security.jwt.JwtTokenProperties;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationDto;
import com.ssafy.aroundthekorea.user.controller.response.LoginResponseDto;
import com.ssafy.aroundthekorea.user.controller.response.TokenDto;
import com.ssafy.aroundthekorea.user.domain.User;
import com.ssafy.aroundthekorea.user.domain.repository.UserRepository;
import com.ssafy.aroundthekorea.user.service.account.AccountService;

@SpringBootTest
class AuthInterceptorTest {
	@Autowired
	AuthInterceptor authInterceptor;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AccountService accountService;

	@Autowired
	JwtTokenProperties jwtTokenProperties;

	@DisplayName("엑세스 토큰 재발급 후 contextHolder claim 반영되고 응답 header 에 access 토큰 값은 새로 갱신되어야 한다.")
	@Test
	void testInContextHolder() throws JsonProcessingException, InterruptedException {
		//given
		String rawPassword = "dsmkao1239!";
		User user = userRepository.save(User.builder()
			.username("docker1784")
			.password(passwordEncoder.encode(rawPassword))
			.email("DOCK133@gmail.com")
			.build());
		LoginResponseDto responseDto = accountService.createTokens(
			new JwtAuthenticationDto(user.getId(), user.getUsername()));
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		TokenDto access = responseDto.access();
		TokenDto refresh = responseDto.refresh();
		request.addHeader(access.headerName(), access.token());
		request.addHeader(refresh.headerName(), refresh.token());
		//when
		long accessExpiryTime = jwtTokenProperties.accessExpirySeconds() * 2000L;
		Thread.sleep(accessExpiryTime);
		authInterceptor.preHandle(request, response, new Object());
		String expectedNewAccessToken = response.getHeader(jwtTokenProperties.accessHeader());
		String expectedImmutableRefresh = response.getHeader(jwtTokenProperties.accessHeader());

		//then
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Assertions.assertThat(authentication.getPrincipal()).isInstanceOf(JwtAuthenticationDto.class);
		JwtAuthenticationDto auth = (JwtAuthenticationDto)authentication.getPrincipal();
		Assertions.assertThat(auth.userId()).isEqualTo(user.getId());
		Assertions.assertThat(auth.username()).isEqualTo(user.getUsername());
		Assertions.assertThat(expectedNewAccessToken).isNotEqualTo(access.token());
		Assertions.assertThat(expectedImmutableRefresh).isNotEqualTo(refresh.token());
	}
}