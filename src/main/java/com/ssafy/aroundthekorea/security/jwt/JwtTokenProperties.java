package com.ssafy.aroundthekorea.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Profile;

@Profile({"local"})
@ConfigurationProperties(prefix = "jwt.token")
public record JwtTokenProperties
	(
		String accessHeader,
		String refreshHeader,
		String issuer,
		String clientSecret,
		int accessExpirySeconds,
		int refreshExpirySeconds
	) {
	@ConstructorBinding
	public JwtTokenProperties {
	}
}
