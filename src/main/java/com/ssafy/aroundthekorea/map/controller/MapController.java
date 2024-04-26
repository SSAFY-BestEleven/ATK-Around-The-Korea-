package com.ssafy.aroundthekorea.map.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/map")
public class MapController {
	
	@Value("${kakaomap.key}")
	String kakaomapKey;
	
	@Value("${tmap.key}")
	String tmapKey;

	@GetMapping("/kakao")
	public ResponseEntity<String> getKakao(HttpServletRequest req) {
		return ResponseEntity.status(HttpStatus.OK).body(kakaomapKey);
	}
	
	
}
