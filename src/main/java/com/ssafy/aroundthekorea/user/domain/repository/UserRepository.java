package com.ssafy.aroundthekorea.user.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.aroundthekorea.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUsername(@Param("username") String username);

	Optional<User> findByUsername(@Param("username") String username);
}
