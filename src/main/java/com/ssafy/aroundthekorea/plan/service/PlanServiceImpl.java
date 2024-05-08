package com.ssafy.aroundthekorea.plan.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.aroundthekorea.plan.domain.TravelPlan;
import com.ssafy.aroundthekorea.plan.domain.TravelPlanOrderRequest;
import com.ssafy.aroundthekorea.plan.repository.TravelPlanRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class PlanServiceImpl implements PlanService {
	private final TravelPlanRepository travelPlanRepository;

	@Override
	public void addContentToPlan(Integer contentId, Integer planId) {
		List<TravelPlan> travelPlanList = travelPlanRepository.findAllByPlanId(planId);
		TravelPlan travelPlan = TravelPlan.builder().attractionInfoId(contentId).planId(planId)
				.orderIndex(travelPlanList.size()).build();
		travelPlanRepository.save(travelPlan);
	}

	@Override
	public void modifyOrder(List<TravelPlanOrderRequest> orderList,Integer planId) {
		for(int i=0;i<orderList.size();i++)
		{
			// orderlist에서 꺼내서 service단에서 repository로 넘겨줄 파라미터를 세팅해야함.
			TravelPlanOrderRequest travelPlanOrderReqeust = orderList.get(i);
//			travelPlanRepository.modifyOrderByAttractionInfoId(travelPlanOrderReqeust,planId);
		}
	}

}
