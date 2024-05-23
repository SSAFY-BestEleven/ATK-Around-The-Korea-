package com.ssafy.aroundthekorea.plan.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TravelPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer attractionInfoId;
	private String attractionInfoTitle;
	private Integer planId;
	private Integer orderIndex; // order 1 이면 출발 가장 끝 값은 도착, 프론트에서 수정 후 반환
	private Double mapx;
	private Double mapy;
}
