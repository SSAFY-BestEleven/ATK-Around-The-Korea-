package com.ssafy.aroundthekorea.plan.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.aroundthekorea.plan.domain.TravelPlan;
import com.ssafy.aroundthekorea.plan.domain.TravelPlanOrderRequest;
import com.ssafy.aroundthekorea.plan.service.PlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PlanController {
	private final PlanService planService;

	// contentId를 선택한 planId에 맞게 계획 추가
	@PostMapping("/{contentId}")
	public ResponseEntity<?> getDetail(@PathVariable("contentId") Integer contentId,
			@RequestParam("planId") Integer planId) {
		planService.addContentToPlan(contentId, planId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	// 클라이언트가 계획표의 순서를 변경 후 DB 반영
	@PatchMapping("/{planId}/travel-plan")
	public ResponseEntity<?> modifyPlan(@RequestBody List<TravelPlanOrderRequest> request,
			@PathVariable("planId") Integer planId) {
		// request의 travelId는 해당 계획의 여행지 id, order는 원래 order이며 해당 인덱스가 바뀔 order
		planService.modifyOrder(request, planId);
		// 새로운 list를 return
		return ResponseEntity.status(HttpStatus.OK).body(request);
	}

	// planId에 따른 TravelPlan 조회
	@GetMapping("/{planId}/travel-plan")
	public ResponseEntity<?> getTravelPlans(@PathVariable("planId") Integer planId) {
		List<TravelPlan> travelPlans = planService.getTravelPlansByPlanId(planId);
		return ResponseEntity.status(HttpStatus.OK).body(travelPlans);
	}

	// 특정 planId에 있는 TravelPlan 삭제
	@DeleteMapping("/{planId}/{travelPlanId}")
	public ResponseEntity<?> deleteTravelPlan(@PathVariable("planId") Integer planId, @PathVariable("travelPlanId") Integer travelPlanId){
		planService.deleteByTravelPlanId(planId,travelPlanId);
		return ResponseEntity.status(HttpStatus.OK).body(travelPlanId);
	}

	// 특정 planId 삭제
	@DeleteMapping("/{planId}")
	public ResponseEntity<?> deletePlan(@PathVariable("planId") Integer planId){
		planService.deleteByPlanId(planId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
