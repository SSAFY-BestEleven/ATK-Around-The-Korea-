package com.ssafy.aroundthekorea.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.aroundthekorea.security.interceptor.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private final AuthInterceptor authInterceptor;

	public WebConfig(AuthInterceptor authInterceptor) {
		this.authInterceptor = authInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor)
			.excludePathPatterns("/api/v1/accounts/**")
			.excludePathPatterns("/swagger-ui.html")
			.excludePathPatterns("/v3/api-docs/**")
			.excludePathPatterns("/swagger-ui/**")
			.excludePathPatterns("/**");
	}
}
