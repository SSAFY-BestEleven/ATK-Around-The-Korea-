package com.ssafy.aroundthekorea.map.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.aroundthekorea.map.domain.AttractionInfo;
import com.ssafy.aroundthekorea.map.repository.AttractionInfoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MapServcieImpl implements MapService {
	private final AttractionInfoRepository attractionInfoRepository;

	@Override
	public List<AttractionInfo> getKeywordAttraction(String keyword) {
		return attractionInfoRepository.findAllByKeyword(keyword);
	}

}
