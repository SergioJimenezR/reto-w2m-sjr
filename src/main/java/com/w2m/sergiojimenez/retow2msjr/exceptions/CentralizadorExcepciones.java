package com.w2m.sergiojimenez.retow2msjr.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CentralizadorExcepciones extends ResponseEntityExceptionHandler {

	/*
	 * Se ha seguido un Patrón de Diseño tal que, todas las excepciones
	 * personalizadas se reconvierten fácilmente por Polimorfismo a
	 * ExceptionHTTPPolimorfica en el throws de cada método del Controller, porque
	 * todas heredan de ella, evitando así tener que implementar aquí decenas de
	 * manejadores de excepciones.
	 */

	@ExceptionHandler(ExceptionHTTPPolimorfica.class)
	public ResponseEntity<Object> handleMyCustomException(ExceptionHTTPPolimorfica exception, WebRequest request) {

		/*
		 * Con este primer caso (throw new ResponseStatusException), seguiríamos
		 * provocando el mensaje de error:
		 */
		/*
		 * throw new ResponseStatusException(exception.getStatus(),
		 * exception.getMessage());
		 */

		/*
		 * En cambio, con este segundo caso, el error no será tan "feo", sino que se
		 * manejará de esta otra forma, mostrando el error:
		 */
		return new ResponseEntity<>(exception.getMessage(), exception.getStatus());

	}

}
