package com.ssafy.aroundthekorea.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.aroundthekorea.map.domain.AttractionInfo;

public interface AttractionInfoRepository extends JpaRepository<AttractionInfo, Integer> {
	// 키워드 검색을 위한 쿼리문 생성(sido와 gugun이 null일 경우 자기자신을 반환하여 모든 sido gugun에 대해 실시)
//	@Query("SELECT a FROM AttractionInfo a WHERE (a.title LIKE %:keyword% OR a.addr1 LIKE %:keyword% OR a.addr2 LIKE %:keyword%) AND a.sidoCode = CASE WHEN :sido IS NOT NULL THEN :sido ELSE a.sidoCode END AND a.contentTypeId = CASE WHEN :contentTypeId IS NOT NULL THEN :contentTypeId ELSE a.contentTypeId END")
	@Query("SELECT a \r\n"
			+ "FROM AttractionInfo a\r\n"
			+ "WHERE (a.title LIKE CONCAT('%', :keyword, '%') OR a.addr1 LIKE CONCAT('%', :keyword, '%') OR a.addr2 LIKE CONCAT('%', :keyword, '%'))\r\n"
			+ "  AND (a.sidoCode = :sido OR :sido = 0)\r\n"
			+ "  AND (a.contentTypeId = :contentTypeId OR :contentTypeId = 0)\r\n")
	List<AttractionInfo> findAllByKeyword(@Param("keyword") String keyword, @Param("sido") Long sido,
			 @Param("contentTypeId") Long contentTypeId);

}
