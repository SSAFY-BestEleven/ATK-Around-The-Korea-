package com.ssafy.aroundthekorea.exception.model;

public class NotFoundResource extends RuntimeException {
	public NotFoundResource(String message) {
		super(message);
	}
}
