package com.ssafy.aroundthekorea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelPlan {
	private Integer id;
	private Integer attractionInfoId;
	private Integer planId;
	private Integer order;
}
