package com.ssafy.aroundthekorea.map.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

	// 키워드 검색
	@GetMapping("/keyword")
	public List<AttractionInfo> getKeywordAttraction(@RequestParam("keyword") String keyword,
			@RequestParam("sido") Long sido, @RequestParam("gugun") Long gugun,
			@RequestParam("contentTypeId") Long contentTypeId) {
		return mapService.getKeywordAttraction(keyword, sido, gugun, contentTypeId);
	}

	// 상세보기 검색
	// https://map.kakao.com/link/from/18577297/to/18577297 from에서 to로 길찾기 지원 가능
	
}
