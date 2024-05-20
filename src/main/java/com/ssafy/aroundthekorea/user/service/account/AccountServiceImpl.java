package com.ssafy.aroundthekorea.user.service.account;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.aroundthekorea.security.jwt.JwtHandler;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationDto;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationToken;
import com.ssafy.aroundthekorea.security.jwt.JwtTokenProperties;
import com.ssafy.aroundthekorea.security.token.domain.JwtToken;
import com.ssafy.aroundthekorea.security.token.repository.JwtTokenRepository;
import com.ssafy.aroundthekorea.user.controller.response.LoginResponseDto;
import com.ssafy.aroundthekorea.user.controller.response.TokenDto;
import com.ssafy.aroundthekorea.user.domain.User;
import com.ssafy.aroundthekorea.user.domain.repository.UserRepository;

@Transactional(readOnly = true)
@Service
public class AccountServiceImpl implements AccountService {
	private static final Logger log = LoggerFactory.getLogger(AnnotationConfigUtils.class);
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtHandler jwtHandler;
	private final JwtTokenProperties jwtTokenProperties;
	private final JwtTokenRepository jwtTokenRepository;

	public AccountServiceImpl(UserRepository userRepository,
		PasswordEncoder passwordEncoder,
		JwtHandler jwtHandler,
		JwtTokenProperties jwtTokenProperties,
		JwtTokenRepository jwtTokenRepository
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtHandler = jwtHandler;
		this.jwtTokenProperties = jwtTokenProperties;
		this.jwtTokenRepository = jwtTokenRepository;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken)authentication;
		String username = String.valueOf(authenticationToken.getPrincipal());
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new BadCredentialsException("아이디가 일치하지 않습니다."));

		String password = (String)authentication.getCredentials();
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new AuthenticationCredentialsNotFoundException("패스워드가 일치하지 않습니다.");
		}
		authenticationToken.setDetails(new JwtAuthenticationDto(user.getId(), user.getUsername()));

		return authenticationToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return AccountServiceImpl.class.isAssignableFrom(authentication);
	}

	@Override
	@Transactional
	public LoginResponseDto createTokens(JwtAuthenticationDto authenticationDto) {
		var accessClaims = JwtHandler.Claims.of(
			authenticationDto.userId(),
			authenticationDto.username(),
			new String[] {"ROLE_USER"});
		var refreshClaims = JwtHandler.Claims.of(authenticationDto.userId());

		String accessToken = null;
		String refreshToken = null;
		accessToken = jwtHandler.createForAccess(accessClaims);
		refreshToken = jwtHandler.createForRefresh(refreshClaims);
		jwtTokenRepository.save(new JwtToken(authenticationDto.userId(), refreshToken));

		return new LoginResponseDto(
			new TokenDto(jwtTokenProperties.accessHeader(), accessToken),
			new TokenDto(jwtTokenProperties.refreshHeader(), refreshToken)
		);
	}

	@Transactional
	@Override
	public void removeToken(Long userId) {
		jwtTokenRepository.deleteByUserId(userId);
	}

	@Override
	public boolean isDuplicate(String username) {
		return userRepository.existsByUsername(username);
	}

}
