package com.ssafy.aroundthekorea.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.aroundthekorea.plan.domain.Plan;

public interface PlanRepository extends JpaRepository<Plan, Integer> {
	
}
