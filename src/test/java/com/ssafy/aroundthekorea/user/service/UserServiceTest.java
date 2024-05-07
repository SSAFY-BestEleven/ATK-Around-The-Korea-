package com.ssafy.aroundthekorea.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ssafy.aroundthekorea.user.controller.request.SignUpUserRequestDto;
import com.ssafy.aroundthekorea.user.domain.repository.UserRepository;
import com.ssafy.aroundthekorea.user.service.mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@InjectMocks
	UserService userService;

	@Mock
	UserRepository userRepository;

	@Spy
	UserMapper userMapper;

	@Mock
	PasswordEncoder passwordEncoder;

	@DisplayName("회원정보가 저장된다.")
	@Test
	void testCreateUser() {
		//given
		var request = new SignUpUserRequestDto("usr123", "{encryption} us!!2jfmksx$", "asr@gmail.com");
		given(userRepository.existsByUsername(request.username())).willReturn(false);
		//when
		userService.create(request);
		//then
		verify(userRepository, times(1)).existsByUsername(request.username());
		verify(userRepository, times(1)).save(any());
	}

	@DisplayName("중복된 username 저장할 수 없다.")
	@Test
	void failCreateUserByDuplicatedUsername() {
		//given
		String duplicatedUsername = "usr123";
		var request = new SignUpUserRequestDto(duplicatedUsername, "{encryption} us!!2jfmksx$", "asr@gmail.com");
		given(userRepository.existsByUsername(request.username())).willReturn(true);
		//when
		//then
		Assertions.assertThatThrownBy(() -> userService.create(request))
			.isInstanceOf(RuntimeException.class);
		verify(userRepository, times(1)).existsByUsername(request.username());
	}

}