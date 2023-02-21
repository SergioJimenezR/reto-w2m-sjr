package com.w2m.sergiojimenez.retow2msjr.exceptions;

import org.springframework.http.HttpStatus;

public class SuperheroeInexistenteException extends ExceptionHTTPPolimorfica {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5282301736380238833L;

	public SuperheroeInexistenteException(HttpStatus status, String reason) {
		super(status, reason);
	}

}
