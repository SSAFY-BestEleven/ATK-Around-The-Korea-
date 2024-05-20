package com.ssafy.aroundthekorea.security.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class PathContainer {
	private final PathMatcher pathMatcher;
	private final List<RequestPath> includePathPattern;
	private final List<RequestPath> excludePathPattern;

	public PathContainer() {
		this.pathMatcher = new AntPathMatcher();
		this.includePathPattern = new ArrayList<>();
		this.excludePathPattern = new ArrayList<>();
	}

	public boolean isExcludePath(String targetPath, String pathMethod) {
		return excludePathPattern.stream()
			.anyMatch(requestPath -> anyMatchPathPattern(targetPath, pathMethod, requestPath));
	}

	private boolean anyMatchPathPattern(String targetPath, String pathMethod, RequestPath requestPath) {
		return pathMatcher.match(requestPath.getPathPattern(), targetPath) &&
			requestPath.matchesMethod(pathMethod);
	}

	public void includePathPattern(String targetPath, PathMethod pathMethod) {
		this.includePathPattern.add(new RequestPath(targetPath, pathMethod));
	}

	public void excludePathPattern(String targetPath, PathMethod pathMethod) {
		this.excludePathPattern.add(new RequestPath(targetPath, pathMethod));
	}
}
