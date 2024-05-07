package com.ssafy.aroundthekorea.user.controller.response;

public record LoginResponseDto(TokenDto access,
							   TokenDto refresh
) {
}
