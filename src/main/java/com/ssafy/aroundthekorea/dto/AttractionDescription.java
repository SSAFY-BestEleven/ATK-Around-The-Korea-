package com.ssafy.aroundthekorea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionDescription {
	private Integer contentId;
	private String homepage;
	private String overview;
	private String telname;
}
