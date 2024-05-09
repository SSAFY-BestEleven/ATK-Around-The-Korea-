package com.ssafy.aroundthekorea.map.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionDetail {
	private Integer contentId;
	private String cat1, cat2, cat3, createdTime, modifiedTime, bookTour;
}
