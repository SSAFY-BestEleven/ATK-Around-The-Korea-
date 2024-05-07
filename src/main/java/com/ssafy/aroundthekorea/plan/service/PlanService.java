package com.ssafy.aroundthekorea.plan.service;

import org.springframework.stereotype.Service;

@Service
public interface PlanService {

	void addContentToPlan(Integer contentId, Integer planId);

}
