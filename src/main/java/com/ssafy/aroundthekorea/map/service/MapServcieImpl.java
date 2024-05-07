package com.ssafy.aroundthekorea.map.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.aroundthekorea.map.domain.AttractionInfo;
import com.ssafy.aroundthekorea.map.repository.AttractionInfoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MapServcieImpl implements MapService {
	private final AttractionInfoRepository attractionInfoRepository;

	@Override
	public List<AttractionInfo> getKeywordAttraction(String keyword, Long sido, Long gugun, Long contentTypeId) {
		try {
			return attractionInfoRepository.findAllByKeyword(keyword, sido, gugun, contentTypeId);
		} catch (Exception e) {
			throw new EntityNotFoundException("해당하는 키워드가 없습니다.");
		}
	}
	
}
