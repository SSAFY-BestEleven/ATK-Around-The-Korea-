package com.ssafy.aroundthekorea.exception.model;

public abstract class BusinessException extends RuntimeException {
	String clientMessage;

	public BusinessException(String message, String clientMessage) {
		super(message);
		this.clientMessage = clientMessage;
	}

	public BusinessException(String message, Throwable cause, String clientMessage) {
		super(message, cause);
		this.clientMessage = clientMessage;
	}

	public String getClientMessage() {
		return clientMessage;
	}
}
