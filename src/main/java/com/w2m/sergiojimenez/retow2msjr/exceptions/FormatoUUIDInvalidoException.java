package com.w2m.sergiojimenez.retow2msjr.exceptions;

import org.springframework.http.HttpStatus;

public class FormatoUUIDInvalidoException extends ExceptionHTTPPolimorfica {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5282301736380238833L;

	public FormatoUUIDInvalidoException(HttpStatus status, String reason) {
		super(status, reason);
	}

}
