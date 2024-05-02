package com.ssafy.aroundthekorea.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.aroundthekorea.map.domain.AttractionInfo;

public interface AttractionInfoRepository extends JpaRepository<AttractionInfo, Long> {
	List<AttractionInfo> findAllByKeyword(String keyword);
}
