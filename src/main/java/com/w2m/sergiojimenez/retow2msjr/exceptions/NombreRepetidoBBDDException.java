package com.w2m.sergiojimenez.retow2msjr.exceptions;

import org.springframework.http.HttpStatus;

public class NombreRepetidoBBDDException extends ExceptionHTTPPolimorfica {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5282301736380238833L;

	public NombreRepetidoBBDDException(HttpStatus status, String reason) {
		super(status, reason);
	}

}
