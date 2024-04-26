package com.ssafy.aroundthekorea.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {
	private Integer planId;
	private Integer userId;
	private Date date, createdAt, modifiedAt;
	private String content;
}
