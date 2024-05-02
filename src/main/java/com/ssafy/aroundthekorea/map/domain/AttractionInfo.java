package com.ssafy.aroundthekorea.map.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionInfo {
	private Long contentId;
	private Long contentTypeId;
	private String title;
	private String addr1;
	private String addr2;
	private String zipcode;
	private String tel;
	private String firstImage;
	private String firstImage2;
	private Long readCount;
	private Long sidoCode;
	private Long gugunCode;
	private Double latitude;
	private Double longitude;
	private String mlevel;
}
