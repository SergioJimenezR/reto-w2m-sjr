package com.w2m.sergiojimenez.retow2msjr.exceptions;

import org.springframework.http.HttpStatus;

public class ExceptionHTTPPolimorfica extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4740023622492285617L;
	
	protected final HttpStatus status;
	protected final String reason;

	public ExceptionHTTPPolimorfica(HttpStatus status, String reason) {
		super(reason);
		this.status = status;
		this.reason = reason;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getReason() {
		return reason;
	}

}
