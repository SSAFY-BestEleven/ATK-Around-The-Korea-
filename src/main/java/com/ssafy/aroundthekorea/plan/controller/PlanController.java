package com.ssafy.aroundthekorea.plan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.aroundthekorea.plan.service.PlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanController {
	private final PlanService planService;

	// contentId를 선택한 planId에 맞게 계획 추가
	@PostMapping("/{contentId}/add")
	public ResponseEntity<?> getDetail(@PathVariable("contentId") Integer contentId,@RequestParam("planId") Integer planId) {
		planService.addContentToPlan(contentId,planId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
