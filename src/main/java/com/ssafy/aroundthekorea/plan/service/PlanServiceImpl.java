package com.ssafy.aroundthekorea.plan.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		 // planId에 맞는 모든 TravelPlan 객체를 가져옴
        List<TravelPlan> travelPlans = travelPlanRepository.findAllByPlanId(planId);

        // orderList를 Map으로 변환하여 attractionInfoId를 키로 매핑
        Map<Integer, Integer> orderMap = orderList.stream()
                .collect(Collectors.toMap(TravelPlanOrderRequest::getAttractionInfoId, TravelPlanOrderRequest::getOrderIndex));

        // TravelPlan 객체들의 orderIndex 업데이트
        for (TravelPlan travelPlan : travelPlans) {
            Integer newOrderIndex = orderMap.get(travelPlan.getAttractionInfoId());
            if (newOrderIndex != null) {
                travelPlan.setOrderIndex(newOrderIndex);
            }
        }

        // 업데이트된 TravelPlan 객체들을 저장
        travelPlanRepository.saveAll(travelPlans);
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
