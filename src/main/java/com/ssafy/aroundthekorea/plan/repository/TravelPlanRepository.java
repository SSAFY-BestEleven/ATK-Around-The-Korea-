package com.ssafy.aroundthekorea.plan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.aroundthekorea.plan.domain.TravelPlan;

public interface TravelPlanRepository extends JpaRepository<TravelPlan, Integer> {
	// planId에 맞는 모든 travelplan을 반환
	@Query("select tp from TravelPlan tp where tp.planId = :planId")
	List<TravelPlan> findAllByPlanId(@Param("planId") Integer planId);

	TravelPlan findByAttractionInfoId(Integer attractionInfoId);

	Optional<TravelPlan> findByAttractionInfoIdAndPlanId(Integer contentId, Integer planId);
	
	@Query("SELECT COALESCE(MAX(tp.orderIndex), 0) FROM TravelPlan tp WHERE tp.planId = :planId")
	Integer getMaxOrderIndex(@Param("planId") Integer planId);

}
