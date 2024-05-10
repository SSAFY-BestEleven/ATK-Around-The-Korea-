package com.ssafy.aroundthekorea.map.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.aroundthekorea.map.domain.AttractionInfo;
import com.ssafy.aroundthekorea.map.service.MapService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MapController {
	private final MapService mapService;

	@Value("${kakaomap.key}")
	String kakaomapKey;

	@Value("${tmap.key}")
	String tmapKey;

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
