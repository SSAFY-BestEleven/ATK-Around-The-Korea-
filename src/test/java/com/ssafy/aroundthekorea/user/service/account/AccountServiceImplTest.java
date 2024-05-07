package com.ssafy.aroundthekorea.user.service.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ssafy.aroundthekorea.security.jwt.JwtHandler;
import com.ssafy.aroundthekorea.security.jwt.JwtTokenProperties;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthentication;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationDto;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationToken;
import com.ssafy.aroundthekorea.security.token.repository.JwtTokenRepository;
import com.ssafy.aroundthekorea.user.controller.response.LoginResponseDto;
import com.ssafy.aroundthekorea.user.controller.response.TokenDto;
import com.ssafy.aroundthekorea.user.domain.User;
import com.ssafy.aroundthekorea.user.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

	@InjectMocks
	AccountServiceImpl accountService;

	@Mock
	UserRepository userRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@Mock
	JwtHandler jwtHandler;

	@Mock
	JwtTokenProperties jwtTokenProperties;

	@Mock
	JwtTokenRepository jwtTokenRepository;

	@DisplayName("인증에 성공한다")
	@Test
	void testAuthenticate() {
		//given
		User expectedUser = User.builder()
			.id(1L)
			.username("dockerize1784")
			.password("{encrypt} njdbsnb23#jaknj1232133#")
			.build();
		String rawPassword = "user plain text password";
		var authentication = new JwtAuthenticationToken(expectedUser.getUsername(), rawPassword);
		given(userRepository.findByUsername(any())).willReturn(Optional.of(expectedUser));
		given(passwordEncoder.matches(rawPassword, expectedUser.getPassword())).willReturn(true);
		//when
		Authentication verifiedAuth = accountService.authenticate(authentication);
		//then
		assertThat(verifiedAuth.getPrincipal()).isInstanceOf(String.class);
		assertThat((String)verifiedAuth.getPrincipal()).isEqualTo(expectedUser.getUsername());
		assertThat(verifiedAuth.getDetails()).isInstanceOf(JwtAuthenticationDto.class);
		JwtAuthenticationDto details = (JwtAuthenticationDto)verifiedAuth.getDetails();
		assertThat(details.userId()).isEqualTo(expectedUser.getId());
		assertThat(details.username()).isEqualTo(expectedUser.getUsername());
	}

	@DisplayName("존재하지 않는 username 이면 인증에 실패한다")
	@Test
	void failAuthenticateByInvalidUsername() {
		//given
		User expectedUser = User.builder()
			.id(1L)
			.username("dockerize1784")
			.password("{encrypt} njdbsnb23#jaknj1232133#")
			.build();
		String rawPassword = "user plain text password";
		var authentication = new JwtAuthenticationToken(expectedUser.getUsername(), rawPassword);
		given(userRepository.findByUsername(any())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> accountService.authenticate(authentication))
			.isInstanceOf(BadCredentialsException.class);
	}

	@DisplayName("비밀번호가 일치하지 않는다면 인증에 실패한다")
	@Test
	void failAuthenticateByInvalidPassword() {
		//given
		User expectedUser = User.builder()
			.id(1L)
			.username("dockerize1784")
			.password("{encrypt} njdbsnb23#jaknj1232133#")
			.build();
		String rawPassword = "user plain text password";
		var authentication = new JwtAuthenticationToken(expectedUser.getUsername(), rawPassword);
		given(userRepository.findByUsername(any())).willReturn(Optional.of(expectedUser));
		given(passwordEncoder.matches(rawPassword, expectedUser.getPassword())).willReturn(false);
		//when
		//then
		assertThatThrownBy(() -> accountService.authenticate(authentication))
			.isInstanceOf(AuthenticationCredentialsNotFoundException.class);
	}

	@DisplayName("클레임 기반으로 2개의 토큰을 생성한다")
	@Test
	void testCreateToken() {
		//given
		JwtAuthenticationDto authenticationDto = new JwtAuthenticationDto(1L, "dockerize1784");
		JwtHandler.Claims expectedAccessClaim = JwtHandler.Claims.of(1L, authenticationDto.username(),
			new String[] {"ROLE_USER"});
		JwtHandler.Claims expectedRefreshClaim = JwtHandler.Claims.of(1L);
		given(jwtHandler.createForAccess(any())).willReturn("claim 기반 엑세스 토큰");
		given(jwtHandler.createForAccess(any())).willReturn("claim 기반 리프레시 토큰");
		given(jwtTokenProperties.accessHeader()).willReturn("access");
		given(jwtTokenProperties.refreshHeader()).willReturn("refresh");
		//when
		accountService.createTokens(authenticationDto);
		//then
		verify(jwtHandler, times(1)).createForAccess(any());
		verify(jwtHandler, times(1)).createForRefresh(any());
		verify(jwtTokenRepository, times(1)).save(any());
		verify(jwtTokenProperties, times(1)).accessHeader();
		verify(jwtTokenProperties, times(1)).refreshHeader();
	}
}