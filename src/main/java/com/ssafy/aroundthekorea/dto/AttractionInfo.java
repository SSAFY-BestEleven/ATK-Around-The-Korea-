package com.ssafy.aroundthekorea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionInfo {
	private Integer contentId;
	private Integer contentTypeId;
	private String title;
	private String addr1;
	private String addr2;
	private String zipcode;
	private String tel;
	private String firstImage;
	private String firstImage2;
	private Integer readCount;
	private Integer sidoCode;
	private Integer gugunCode;
	private Double latitude;
	private Double longitude;
	private String mlevel;
}
