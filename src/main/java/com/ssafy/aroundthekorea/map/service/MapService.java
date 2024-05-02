package com.ssafy.aroundthekorea.map.service;

import java.util.List;

import com.ssafy.aroundthekorea.map.domain.AttractionInfo;

public interface MapService {

	List<AttractionInfo> getKeywordAttraction(String keyword);

}
