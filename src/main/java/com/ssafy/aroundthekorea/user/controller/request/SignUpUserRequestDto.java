package com.ssafy.aroundthekorea.user.controller.request;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignUpUserRequestDto(
	@NotBlank String username,
	@NotBlank String password,
	@NotBlank @Email String email
) {

}
