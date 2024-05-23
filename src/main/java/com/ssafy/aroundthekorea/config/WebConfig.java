package com.ssafy.aroundthekorea.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.aroundthekorea.security.interceptor.AuthInterceptor;
import com.ssafy.aroundthekorea.security.interceptor.PathMatcherInterceptor;
import com.ssafy.aroundthekorea.security.interceptor.PathMethod;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private final AuthInterceptor authInterceptor;

	public WebConfig(AuthInterceptor authInterceptor) {
		this.authInterceptor = authInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(proxyInterceptor())
		.excludePathPatterns("/api/v1/accounts/**")
		.excludePathPatterns("/swagger-ui.html")
		.excludePathPatterns("/v3/api-docs/**")
		.excludePathPatterns("/swagger-ui/**")
		.excludePathPatterns("/**");
	}

	public HandlerInterceptor proxyInterceptor() {
		return new PathMatcherInterceptor(authInterceptor)
			.excludePathPattern("/api/v1/accounts/**", PathMethod.POST)
			.excludePathPattern("/api/v1/accounts/**", PathMethod.GET)
			.excludePathPattern("/h2-console/**", PathMethod.ANY);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
			.allowCredentials(false)
			.allowedOrigins("http://localhost:3000")
			.allowedMethods("*");
	}
}
