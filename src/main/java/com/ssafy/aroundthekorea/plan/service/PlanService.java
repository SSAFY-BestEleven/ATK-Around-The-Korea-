package com.ssafy.aroundthekorea.plan.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.aroundthekorea.plan.domain.TravelPlan;
import com.ssafy.aroundthekorea.plan.domain.TravelPlanOrderRequest;

@Service
public interface PlanService {

	void addContentToPlan(Integer contentId, Integer planId);

	void modifyOrder(List<TravelPlanOrderRequest> request,Integer planId);

	List<TravelPlan> getTravelPlansByPlanId(Integer planId);

	void deleteByTravelPlanId( Integer travelPlanId);

	void deleteByPlanId(Integer planId);

}
