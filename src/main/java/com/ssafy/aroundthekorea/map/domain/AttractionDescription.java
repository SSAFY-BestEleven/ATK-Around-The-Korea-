package com.ssafy.aroundthekorea.map.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AttractionDescription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer contentId;
	private String homepage;
	private String overview;
	private String telname;
}
