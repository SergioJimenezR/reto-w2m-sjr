package com.w2m.sergiojimenez.retow2msjr.exceptions;

import org.springframework.http.HttpStatus;

public class UUIDInvalidoException extends ExceptionHTTPPolimorfica {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5282301736380238833L;

	public UUIDInvalidoException(HttpStatus status, String reason) {
		super(status, reason);
	}

}
