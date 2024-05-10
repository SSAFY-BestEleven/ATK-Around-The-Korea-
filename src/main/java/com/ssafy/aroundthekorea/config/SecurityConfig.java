package com.ssafy.aroundthekorea.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ssafy.aroundthekorea.security.custom.OpenPolicyAgentAuthorizationManager;
import com.ssafy.aroundthekorea.security.jwt.JwtHandler;
import com.ssafy.aroundthekorea.security.jwt.JwtTokenProperties;

@EnableConfigurationProperties({JwtTokenProperties.class})
@Configuration
public class SecurityConfig {
	private final JwtTokenProperties jwtTokenProperties;
	private final OpenPolicyAgentAuthorizationManager openPolicyAgentAuthorizationManager;

	public SecurityConfig(JwtTokenProperties jwtTokenProperties,
		OpenPolicyAgentAuthorizationManager openPolicyAgentAuthorizationManager) {
		this.jwtTokenProperties = jwtTokenProperties;
		this.openPolicyAgentAuthorizationManager = openPolicyAgentAuthorizationManager;
	}

	@Bean
	public JwtHandler jwt() {
		return new JwtHandler(jwtTokenProperties.issuer(),
			jwtTokenProperties.clientSecret(),
			jwtTokenProperties.accessExpirySeconds(),
			jwtTokenProperties.refreshExpirySeconds()
		);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.
			logout(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(AbstractHttpConfigurer::disable)
			.oidcLogout(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(
				authRequest -> authRequest.requestMatchers(HttpMethod.POST, "/api/**").permitAll()
					.anyRequest().access(openPolicyAgentAuthorizationManager))
			.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(PathRequest.toH2Console())
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
			.requestMatchers("/swagger-ui/**");
			 
	}
}
