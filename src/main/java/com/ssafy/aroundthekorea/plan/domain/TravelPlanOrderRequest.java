package com.ssafy.aroundthekorea.plan.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelPlanOrderRequest {
	private Integer attractionInfoId;
	private Integer orderIndex;
}
