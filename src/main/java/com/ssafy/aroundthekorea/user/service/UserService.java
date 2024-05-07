package com.ssafy.aroundthekorea.user.service;

import static java.text.MessageFormat.*;

import java.text.MessageFormat;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.aroundthekorea.exception.model.account.AccountException;
import com.ssafy.aroundthekorea.user.controller.request.SignUpUserRequestDto;
import com.ssafy.aroundthekorea.user.domain.User;
import com.ssafy.aroundthekorea.user.domain.repository.UserRepository;
import com.ssafy.aroundthekorea.user.service.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	public void create(SignUpUserRequestDto requestDto) {
		boolean isDuplicate = userRepository.existsByUsername(requestDto.username());
		if (isDuplicate) {
			throw new AccountException(
				format("중복된 아이디를 사용하려 합니다. username -> {0}", requestDto.username())
				, "아이디가 중복되었습니다."
			);
		}

		User user = userMapper.toUser(requestDto);
		user.encrypt(passwordEncoder);
		userRepository.save(user);
	}

}
