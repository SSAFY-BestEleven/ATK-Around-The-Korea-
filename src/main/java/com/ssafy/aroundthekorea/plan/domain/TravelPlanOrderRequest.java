package com.ssafy.aroundthekorea.plan.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class TravelPlanOrderRequest {
	private int travelId;
	private int order;
}
