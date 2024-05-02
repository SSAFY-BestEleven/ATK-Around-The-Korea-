package com.ssafy.aroundthekorea.map.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.aroundthekorea.map.domain.AttractionInfo;
import com.ssafy.aroundthekorea.map.service.MapService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
public class MapController {
	private final MapService mapService;

	@Value("${kakaomap.key}")
	String kakaomapKey;

	@Value("${tmap.key}")
	String tmapKey;

	@GetMapping("/kakao")
	public ResponseEntity<String> getKakao(HttpServletRequest req) {
		return ResponseEntity.status(HttpStatus.OK).body(kakaomapKey);
	}

	@GetMapping("/keyword")
	// 키워드 검색
	public List<AttractionInfo> getKeywordAttraction(@RequestParam("keyword") String keyword) {
		return mapService.getKeywordAttraction(keyword);
	}
}
