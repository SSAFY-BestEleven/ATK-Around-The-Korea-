package com.ssafy.aroundthekorea.plan.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.aroundthekorea.plan.domain.TravelPlan;
import com.ssafy.aroundthekorea.plan.repository.PlanRepository;
import com.ssafy.aroundthekorea.plan.repository.TravelPlanRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
//@Transactional(readOnly = true)
public class PlanServiceImpl implements PlanService {
	private final TravelPlanRepository travelPlanRepository;

	@Override
	public void addContentToPlan(Integer contentId, Integer planId) {
		List<TravelPlan> travelPlanList = travelPlanRepository.findAllByPlanId(planId);
		TravelPlan travelPlan = TravelPlan.builder().attractionInfoId(contentId).planId(planId)
				.orderIndex(travelPlanList.size()).build();
		travelPlanRepository.save(travelPlan);
	}

}
