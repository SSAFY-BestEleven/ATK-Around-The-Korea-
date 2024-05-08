package com.ssafy.aroundthekorea.plan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.aroundthekorea.plan.domain.TravelPlan;
import com.ssafy.aroundthekorea.plan.domain.TravelPlanOrderRequest;

public interface TravelPlanRepository extends JpaRepository<TravelPlan, Integer> {
	// planId에 맞는 모든 travelplan을 반환
	@Query("select tp from TravelPlan tp where tp.planId = :planId")
	List<TravelPlan> findAllByPlanId(@Param("planId") Integer planId);

	@Query("")
	void modifyOrderByAttractionInfoId(
			@Param("planId") Integer planId);

}
