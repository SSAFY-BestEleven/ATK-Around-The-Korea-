package com.ssafy.aroundthekorea.exception.model.account;

import com.ssafy.aroundthekorea.exception.model.BusinessException;

public class AccountException extends BusinessException {
	public AccountException(String message, String clientMessage) {
		super(message, clientMessage);
	}

	public AccountException(String message, Throwable cause, String clientMessage) {
		super(message, cause, clientMessage);
	}
}
