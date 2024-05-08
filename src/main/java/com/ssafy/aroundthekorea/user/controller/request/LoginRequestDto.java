package com.ssafy.aroundthekorea.user.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(@NotNull @NotBlank String username,
							  @NotNull @NotBlank String password) {
}
