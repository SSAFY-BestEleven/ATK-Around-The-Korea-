package com.ssafy.aroundthekorea.map.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name ="attractionInfo")
@Entity
public class AttractionInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@Builder
	public AttractionInfo(Long contentId, Long contentTypeId, String title, String addr1, String addr2, String zipcode,
			String tel, String firstImage, String firstImage2, Long readCount, Long sidoCode, Long gugunCode,
			Double latitude, Double longitude, String mlevel) {
		super();
		this.contentId = contentId;
		this.contentTypeId = contentTypeId;
		this.title = title;
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.zipcode = zipcode;
		this.tel = tel;
		this.firstImage = firstImage;
		this.firstImage2 = firstImage2;
		this.readCount = readCount;
		this.sidoCode = sidoCode;
		this.gugunCode = gugunCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.mlevel = mlevel;
	}

}
