package com.ssafy.aroundthekorea.user.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.aroundthekorea.config.SecurityConfig;
import com.ssafy.aroundthekorea.security.custom.OpenPolicyAgentAuthorizationManager;
import com.ssafy.aroundthekorea.security.jwt.Jwt;
import com.ssafy.aroundthekorea.security.jwt.JwtTokenConfig;
import com.ssafy.aroundthekorea.security.token.TokenService;
import com.ssafy.aroundthekorea.user.controller.request.SignUpUserRequestDto;
import com.ssafy.aroundthekorea.user.domain.repository.UserRepository;
import com.ssafy.aroundthekorea.user.service.UserService;

@Import({SecurityConfig.class, H2ConsoleAutoConfiguration.class})
@WebMvcTest(AccountController.class)
class AccountControllerTest {
	final String PREFIX_URI = "/api/v1/accounts";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	UserService userService;

	@MockBean
	JwtTokenConfig jwtTokenConfig;

	@MockBean
	TokenService tokenService;

	@MockBean
	UserRepository userRepository;

	@MockBean
	Jwt jwt;

	@MockBean
	OpenPolicyAgentAuthorizationManager openPolicyAgentAuthorizationManager;

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

		@DisplayName("username 에 공백이 포함되거나 null값이 들어오면 예외가 발생한다.")
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

		@DisplayName("username 에 공백이 포함되거나 null값이 들어오면 예외가 발생한다.")
		@ParameterizedTest(name = "{index}: invalid password -> [{0}]")
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
}