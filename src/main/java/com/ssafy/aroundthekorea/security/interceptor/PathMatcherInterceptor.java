package com.ssafy.aroundthekorea.security.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PathMatcherInterceptor implements HandlerInterceptor {

	private final HandlerInterceptor handlerInterceptor;
	private final PathContainer pathContainer;

	public PathMatcherInterceptor(HandlerInterceptor handlerInterceptor) {
		this.handlerInterceptor = handlerInterceptor;
		this.pathContainer = new PathContainer();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {
		if (pathContainer.isExcludePath(request.getRequestURI(), request.getMethod())) {
			return true;
		}

		return handlerInterceptor.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
		HttpServletResponse response,
		Object handler,
		ModelAndView modelAndView) throws Exception {
		handlerInterceptor.postHandle(request, response, handler, modelAndView);
	}

	public PathMatcherInterceptor includePathPattern(String pathPattern, PathMethod pathMethod) {
		pathContainer.includePathPattern(pathPattern, pathMethod);
		return this;
	}

	public PathMatcherInterceptor excludePathPattern(String pathPattern, PathMethod pathMethod) {
		pathContainer.excludePathPattern(pathPattern, pathMethod);
		return this;
	}
}