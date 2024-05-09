package com.ssafy.aroundthekorea.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.aroundthekorea.config.SecurityConfig;
import com.ssafy.aroundthekorea.config.WebConfig;
import com.ssafy.aroundthekorea.security.custom.OpenPolicyAgentAuthorizationManager;
import com.ssafy.aroundthekorea.security.interceptor.AuthInterceptor;
import com.ssafy.aroundthekorea.security.jwt.JwtHandler;
import com.ssafy.aroundthekorea.security.jwt.JwtTokenProperties;
import com.ssafy.aroundthekorea.security.jwt.config.WithJwtMockUser;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationDto;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationToken;
import com.ssafy.aroundthekorea.security.token.service.TokenService;
import com.ssafy.aroundthekorea.user.controller.request.LoginRequestDto;
import com.ssafy.aroundthekorea.user.controller.request.SignUpUserRequestDto;
import com.ssafy.aroundthekorea.user.controller.response.LoginResponseDto;
import com.ssafy.aroundthekorea.user.controller.response.TokenDto;
import com.ssafy.aroundthekorea.user.domain.repository.UserRepository;
import com.ssafy.aroundthekorea.user.service.UserService;
import com.ssafy.aroundthekorea.user.service.account.AccountService;

@Import({SecurityConfig.class, H2ConsoleAutoConfiguration.class})
@WebMvcTest(value = AccountController.class, excludeAutoConfiguration = {WebConfig.class})
class AccountControllerTest {
	final String PREFIX_URI = "/api/v1/accounts";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	UserService userService;

	@MockBean
	JwtTokenProperties jwtTokenProperties;

	@MockBean
	TokenService tokenService;

	@MockBean
	UserRepository userRepository;

	@MockBean
	JwtHandler jwtHandler;

	@MockBean
	AuthInterceptor authInterceptor;

	@MockBean
	OpenPolicyAgentAuthorizationManager openPolicyAgentAuthorizationManager;

	@MockBean
	AccountService accountService;

	@DisplayName("회원가입에 성공한다.")
	@Test
	void testSignUp() throws Exception {
		//given
		var requestDto = new SignUpUserRequestDto("docker123", "{encryption} dn1!323nj@", "dock123123@naver.com");
		String body = objectMapper.writeValueAsString(requestDto);
		//when
		//then
		mockMvc.perform(post(PREFIX_URI).contentType(APPLICATION_JSON).content(body))
			.andExpect(status().isOk());
	}

	@DisplayName("[validation] 회원가입 시 ")
	@Nested
	class SignUpValidationTest {
		@DisplayName("username 에 공백이 포함되거나 null값이 들어오면 예외가 발생한다.")
		@ParameterizedTest(name = "{index}: invalid username -> [{0}]")
		@NullAndEmptySource
		void failInvalidUsername(String invalidUsername) throws Exception {
			//given
			var request = new SignUpUserRequestDto(invalidUsername,
				"{encryption} dn1!323nj@",
				"dock123123@naver.com");
			String body = objectMapper.writeValueAsString(request);
			//when
			ResultActions action = getPerform(body);
			//then
			action.andExpect(status().isBadRequest());
		}

		@DisplayName("password 에 공백이 포함되거나 null값이 들어오면 예외가 발생한다.")
		@ParameterizedTest(name = "{index}: invalid password -> [{0}]")
		@NullAndEmptySource
		void failInvalidPassword(String invalidPassword) throws Exception {
			//given
			var request = new SignUpUserRequestDto("docker123",
				invalidPassword,
				"dock123123@naver.com");
			String body = objectMapper.writeValueAsString(request);
			//when
			ResultActions action = getPerform(body);
			//then
			action.andExpect(status().isBadRequest());
		}

		@DisplayName("email 에 공백이 포함되거나 null값이 들어오면 예외가 발생한다.")
		@ParameterizedTest(name = "{index}: invalid email -> [{0}]")
		@NullAndEmptySource
		@ValueSource(strings = {"e.com", "sdmkalsd@@google", "!#!#!###!#.!##!#", "_____", "@gmail.com"})
		void failInvalidEmail(String invalidEmail) throws Exception {
			//given
			var request = new SignUpUserRequestDto("docker123",
				"{encryption} dn1!323nj@",
				invalidEmail);
			String body = objectMapper.writeValueAsString(request);
			//when
			ResultActions action = getPerform(body);
			//then
			action.andExpect(status().isBadRequest());
		}

		private ResultActions getPerform(String body) throws Exception {
			return mockMvc.perform(post(PREFIX_URI).contentType(APPLICATION_JSON).content(body));
		}
	}

	@DisplayName("로그인이 완료되면 토큰 두개를 반환한다.")
	@Test
	void testLogin() throws Exception {
		//given
		var requestDto = new LoginRequestDto("docker123", "{encryption} dn1!323nj@");
		var authenticationToken = new JwtAuthenticationToken(requestDto.username(), requestDto.password());
		authenticationToken.setDetails(new JwtAuthenticationDto(1L, requestDto.username()));
		LoginResponseDto expectedResponse = new LoginResponseDto(
			new TokenDto("access-token", "{header}.{payload}.{signature}"),
			new TokenDto("refresh-token", "{header}.{payload}.{signature}")
		);
		given(accountService.authenticate(any())).willReturn(authenticationToken);
		given(accountService.createTokens(any())).willReturn(expectedResponse);
		String body = objectMapper.writeValueAsString(requestDto);
		//when
		var perform = mockMvc.perform(post(PREFIX_URI + "/login").contentType(APPLICATION_JSON).content(body));
		//then
		String responseBody = perform.andExpect(status().isOk())
			.andReturn()
			.getResponse().getContentAsString();
		LoginResponseDto responseDto = objectMapper.readValue(responseBody, new TypeReference<>() {
		});

		assertThat(responseDto.access().headerName()).isEqualTo(expectedResponse.access().headerName());
		assertThat(responseDto.access().token()).isEqualTo(expectedResponse.access().token());
		assertThat(responseDto.refresh().headerName()).isEqualTo(expectedResponse.refresh().headerName());
		assertThat(responseDto.refresh().token()).isEqualTo(expectedResponse.refresh().token());
	}

	@DisplayName("[Validation] 로그인 시")
	@Nested
	class LoginValidationTest {
		@DisplayName("username 에 공백이 포함되거나 null값이 들어오면 예외가 발생한다.")
		@ParameterizedTest(name = "{index}: invalid username -> [{0}]")
		@NullAndEmptySource
		void failInvalidUsername(String invalidUsername) throws Exception {
			//given
			var requestDto = new LoginRequestDto(invalidUsername, "{encryption} dn1!323nj@");
			String body = objectMapper.writeValueAsString(requestDto);
			//when
			ResultActions action = getPerform(body);
			//then
			action.andExpect(status().isBadRequest());
		}

		@DisplayName("password 에 공백이 포함되거나 null값이 들어오면 예외가 발생한다.")
		@ParameterizedTest(name = "{index}: invalid password -> [{0}]")
		@NullAndEmptySource
		void failInvalidPassword(String invalidPassword) throws Exception {
			//given
			var requestDto = new LoginRequestDto("docker123", invalidPassword);
			String body = objectMapper.writeValueAsString(requestDto);
			//when
			ResultActions action = getPerform(body);
			//then
			action.andExpect(status().isBadRequest());
		}

		private ResultActions getPerform(String body) throws Exception {
			return mockMvc.perform(post(PREFIX_URI + "/login").contentType(APPLICATION_JSON).content(body));
		}
	}

	@WithJwtMockUser
	@DisplayName("로그인한 사용자 많이 로그아웃한다")
	@Test
	void testLogout() throws Exception {
		//given
		given(authInterceptor.preHandle(any(),any(),any())).willReturn(true);
		//when
		mockMvc.perform(delete(PREFIX_URI + "/logout")
				.header("acc","{token}"))
			.andExpect(status().isOk());
		//then
		verify(accountService, times(1)).removeToken(any());
	}
}