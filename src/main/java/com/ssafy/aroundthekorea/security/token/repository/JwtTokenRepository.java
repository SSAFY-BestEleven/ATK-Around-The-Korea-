package com.ssafy.aroundthekorea.security.token.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.aroundthekorea.security.token.domain.JwtToken;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

	@Modifying
	@Query("delete from JwtToken token where token.createdAt < :threshHold")
	void deleteExpiredToken(@Param("threshHold") LocalDateTime threshHold);

	void deleteByUserId(Long userId);

}
