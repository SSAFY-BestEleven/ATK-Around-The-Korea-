package com.ssafy.aroundthekorea.map.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.aroundthekorea.map.domain.AttractionInfo;

@Service
public interface MapService {

	List<AttractionInfo> getKeywordAttraction(String keyword, Long sido, Long gugun,Long contentTypeId);

}
