package com.ssafy.aroundthekorea.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.aroundthekorea.user.controller.request.SignUpUserRequestDto;
import com.ssafy.aroundthekorea.user.domain.User;
import com.ssafy.aroundthekorea.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public void create(SignUpUserRequestDto requestDto) {
		boolean isDuplicate = userRepository.existsByUsername(requestDto.username());
		if (isDuplicate) {
			throw new RuntimeException("아이디가 중복되었습니다.");
		}

		User user = userMapper.toUser(requestDto);
		userRepository.save(user);
	}

}
