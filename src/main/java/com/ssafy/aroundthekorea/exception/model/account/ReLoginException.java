package com.ssafy.aroundthekorea.exception.model.account;

import com.ssafy.aroundthekorea.exception.model.account.AccountException;

public class ReLoginException extends AccountException {
	public ReLoginException(String message, String clientMessage) {
		super(message, clientMessage);
	}

	public ReLoginException(String message, Throwable cause, String clientMessage) {
		super(message, cause, clientMessage);
	}
}
