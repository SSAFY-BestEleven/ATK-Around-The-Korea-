package com.ssafy.aroundthekorea.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
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
		registry.addInterceptor(proxyInterceptor());
	}

	public HandlerInterceptor proxyInterceptor() {
		return new PathMatcherInterceptor(authInterceptor)
			.excludePathPattern("/api/v1/accounts/**", PathMethod.POST);
	}
}
