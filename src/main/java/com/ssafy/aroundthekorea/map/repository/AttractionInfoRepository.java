package com.ssafy.aroundthekorea.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.aroundthekorea.map.domain.AttractionInfo;

public interface AttractionInfoRepository extends JpaRepository<AttractionInfo, Long> {
	// 키워드 검색을 위한 쿼리문 생성
	@Query("SELECT a FROM AttractionInfo a WHERE a.title LIKE %:keyword% OR a.addr1 LIKE %:keyword% OR a.addr2 LIKE %:keyword%")
	List<AttractionInfo> findAllByKeyword(@Param("keyword") String keyword);
}
