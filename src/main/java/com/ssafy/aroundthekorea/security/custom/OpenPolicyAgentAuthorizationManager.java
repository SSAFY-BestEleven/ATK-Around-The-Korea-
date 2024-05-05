package com.ssafy.aroundthekorea.security.custom;

import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

@Component
public class OpenPolicyAgentAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
		return new AuthorizationDecision(true);
	}
}
