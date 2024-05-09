package com.ssafy.aroundthekorea.security.jwt;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.EqualsAndHashCode;
import lombok.Getter;

public final class JwtHandler {

	private final String issuer;

	private final String clientSecret;

	private final int accessExpirySeconds;
	private final int refreshExpirySeconds;

	private final Algorithm algorithm;

	private final JWTVerifier jwtVerifier;

	public JwtHandler(String issuer, String clientSecret, int accessExpirySeconds, int refreshExpirySeconds) {
		this.issuer = issuer;
		this.clientSecret = clientSecret;
		this.accessExpirySeconds = accessExpirySeconds;
		this.refreshExpirySeconds = refreshExpirySeconds;
		this.algorithm = Algorithm.HMAC512(this.clientSecret);
		this.jwtVerifier = JWT.require(algorithm)
			.withIssuer(issuer)
			.build();
	}

	public String createForAccess(Claims claims) {
		Date now = new Date();
		JWTCreator.Builder builder = JWT.create();
		builder.withIssuer(issuer);
		builder.withIssuedAt(now);
		builder.withExpiresAt(new Date(now.getTime() + accessExpirySeconds * 1_000L));
		builder.withClaim("userId", claims.userId);
		builder.withClaim("username", claims.username);
		builder.withArrayClaim("roles", claims.roles);
		return builder.sign(algorithm);
	}

	public String createForRefresh(Claims claims) {
		Date now = new Date();
		JWTCreator.Builder builder = JWT.create();
		builder.withIssuer(issuer);
		builder.withIssuedAt(now);
		builder.withExpiresAt(new Date(now.getTime() + refreshExpirySeconds * 1_000L));
		builder.withClaim("userId", claims.userId);
		return builder.sign(algorithm);
	}

	public Claims verify(String token) throws JWTVerificationException {
		return new Claims(jwtVerifier.verify(token));
	}

	@Getter
	@EqualsAndHashCode
	static public class Claims {
		private Long userId;
		private String username;
		private String[] roles;
		private Date issuedAt;
		private Date expiredAt;

		private Claims() {/*empty*/}

		Claims(DecodedJWT decodedJWT) {
			Claim userId = decodedJWT.getClaim("userId");
			if (!userId.isNull()) {
				this.userId = userId.asLong();
			}
			Claim username = decodedJWT.getClaim("username");
			if (!username.isNull()) {
				this.username = username.asString();
			}

			Claim roles = decodedJWT.getClaim("roles");
			if (!roles.isNull()) {
				this.roles = roles.asArray(String.class);
			}
			this.issuedAt = decodedJWT.getIssuedAt();
			this.expiredAt = decodedJWT.getExpiresAt();
		}

		/**
		 * @Return: for access-token
		 */
		public static Claims of(long userId, String username, String[] roles) {
			Claims claims = new Claims();
			claims.userId = userId;
			claims.username = username;
			claims.roles = roles;
			return claims;
		}

		/**
		 * @Return: for refresh-token
		 */
		public static Claims of(long userId) {
			Claims claims = new Claims();
			claims.userId = userId;
			return claims;
		}
	}

}
