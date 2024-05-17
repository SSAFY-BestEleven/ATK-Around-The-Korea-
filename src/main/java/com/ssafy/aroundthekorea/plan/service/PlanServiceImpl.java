package com.ssafy.aroundthekorea.plan.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.aroundthekorea.exception.model.NotFoundResource;
import com.ssafy.aroundthekorea.plan.domain.TravelPlan;
import com.ssafy.aroundthekorea.plan.domain.TravelPlanOrderRequest;
import com.ssafy.aroundthekorea.plan.repository.PlanRepository;
import com.ssafy.aroundthekorea.plan.repository.TravelPlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
	private final TravelPlanRepository travelPlanRepository;
	private final PlanRepository planRepository;
	@Override
	public void addContentToPlan(Integer contentId, Integer planId) {
		List<TravelPlan> travelPlanList = travelPlanRepository.findAllByPlanId(planId);
		TravelPlan travelPlan = TravelPlan.builder().attractionInfoId(contentId).planId(planId)
				.orderIndex(travelPlanList.size()).build();
		travelPlanRepository.save(travelPlan);
	}

	@Override
	public void modifyOrder(List<TravelPlanOrderRequest> orderList, Integer planId) {
		for (int i = 0; i < orderList.size(); i++) {
			TravelPlanOrderRequest travelPlanOrderReqeust = orderList.get(i);
			TravelPlan travelPlan = travelPlanRepository
					.findByAttractionInfoId(travelPlanOrderReqeust.getAttractionInfoId());
			travelPlan.setOrderIndex(travelPlanOrderReqeust.getOrderIndex());
			travelPlanRepository.save(travelPlan);
		}
	}

	@Override
	public List<TravelPlan> getTravelPlansByPlanId(Integer planId) {
		 List<TravelPlan> travelPlans = travelPlanRepository.findAllByPlanId(planId);
		    if (travelPlans.isEmpty()) {
		        throw new NotFoundResource(planId + "에 해당하는 planId가 없습니다.");
		    }
		    return travelPlans;
	}

	@Override
	public void deleteByTravelPlanId( Integer travelPlanId) {
		travelPlanRepository.deleteById(travelPlanId);
	}

	@Override
	public void deleteByPlanId(Integer planId) {
		planRepository.deleteById(planId);
	}


}
