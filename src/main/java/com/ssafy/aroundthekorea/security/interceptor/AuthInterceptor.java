package com.ssafy.aroundthekorea.security.interceptor;

import static java.text.MessageFormat.format;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ssafy.aroundthekorea.security.token.service.TokenService;
import com.ssafy.aroundthekorea.security.token.dto.response.TokenResponseDto;
import com.ssafy.aroundthekorea.exception.model.NotFoundResource;
import com.ssafy.aroundthekorea.exception.model.account.ReLoginException;
import com.ssafy.aroundthekorea.security.jwt.JwtHandler;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationDto;
import com.ssafy.aroundthekorea.security.jwt.dto.JwtAuthenticationToken;
import com.ssafy.aroundthekorea.security.jwt.JwtTokenProperties;
import com.ssafy.aroundthekorea.user.domain.User;
import com.ssafy.aroundthekorea.user.domain.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
	private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
	private final JwtTokenProperties jwtConfig;
	private final TokenService tokenService;
	private final UserRepository userRepository;
	private final JwtHandler jwtHandler;

	public AuthInterceptor(JwtTokenProperties jwtConfig, TokenService tokenService, UserRepository userRepository,
		JwtHandler jwtHandler) {
		this.jwtConfig = jwtConfig;
		this.tokenService = tokenService;
		this.userRepository = userRepository;
		this.jwtHandler = jwtHandler;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
		HttpServletResponse response,
		Object handler) throws Exception {
		String accessToken = request.getHeader(jwtConfig.accessHeader());
		String refreshToken = request.getHeader(jwtConfig.refreshHeader());

		boolean isExistHeader = accessToken != null && refreshToken != null;
		if (!isExistHeader) {
			throw new BadCredentialsException("token 정보가 올바르지 않습니다.");
		}

		boolean isBlankHeader = accessToken.isBlank() || refreshToken.isBlank();
		if (isBlankHeader) {
			throw new BadCredentialsException("token 정보가 올바르지 않습니다.");
		}

		JwtHandler.Claims verifiedClaim = null;
		try {
			verifiedClaim = jwtHandler.verify(accessToken);
		} catch (TokenExpiredException e) {
			log.info("access token expired! {} progress verify refresh...", e.getMessage());
			reIssueAccessToken(response, refreshToken);
			return true;
		}

		JwtAuthenticationDto jwtAuth = new JwtAuthenticationDto(verifiedClaim.getUserId(), verifiedClaim.getUsername());
		JwtAuthenticationToken jwtAuthToken = new JwtAuthenticationToken(jwtAuth, null);
		SecurityContextHolder.getContext().setAuthentication(jwtAuthToken);
		addHeader(response, accessToken, refreshToken);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
		HttpServletResponse response,
		Object handler,
		ModelAndView modelAndView) {
		SecurityContextHolder.clearContext();
	}

	private void reIssueAccessToken(HttpServletResponse response, String refreshToken) {

		try {
			TokenResponseDto jwtRefreshToken = verifyRefreshToken(refreshToken);
			User user = userRepository.findById(jwtRefreshToken.userId())
				.orElseThrow(() -> new NotFoundResource(
					format("존재하지 않는 리소스입니다. [userid -> {0}]", jwtRefreshToken.userId()))
				);
			JwtHandler.Claims accessClaim = JwtHandler.Claims.of(user.getId(), user.getUsername(), new String[] {"ROLE_USER"});
			String newAccessToken = jwtHandler.createForAccess(accessClaim);
			addHeader(response, newAccessToken, refreshToken);
		} catch (TokenExpiredException | NotFoundResource e) {
			tokenService.deleteExpiredToken();
			throw new ReLoginException(
				"refresh token이 만료되어 재인증이 필요합니다.",
				"다시 로그인 해주세요."
			);
		}
	}

	private TokenResponseDto verifyRefreshToken(String refreshToken) {
		JwtHandler.Claims verifiedRefreshClaim = jwtHandler.verify(refreshToken);

		TokenResponseDto jwtRefreshToken = null;
		try {
			jwtRefreshToken = tokenService.getToken(verifiedRefreshClaim.getUserId());
		} catch (NotFoundResource e) {
			throw new TokenExpiredException("refresh token 만료되었습니다.", Instant.now());
		}

		if (!jwtRefreshToken.token().equals(refreshToken)) {
			throw new BadCredentialsException("[refresh token] 내부적으로 발급한 토큰과 사용자에게 보낸 토큰이 다릅니다.");
		}

		return jwtRefreshToken;
	}

	private void addHeader(HttpServletResponse response, String accessToken, String refreshToken) {
		response.addHeader(jwtConfig.accessHeader(), accessToken);
		response.addHeader(jwtConfig.refreshHeader(), refreshToken);
	}
}
