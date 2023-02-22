package com.w2m.sergiojimenez.retow2msjr.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.codec.digest.DigestUtils;

public class Utilidades {

	private Utilidades() {
	}

	public static String cifrarCadena(String cadena) {
		return DigestUtils.sha3_512Hex(cadena);
	}

	private static final String DATE_FORMAT_PATTERN = "dd-MM-yyyy HH:mm:ss";
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

	public static String parseLocalDateTimeToString(LocalDateTime fecha) {
		return fecha.format(formatter);
	}

	public static LocalDateTime parseStringToLocalDateTime(String strFecha) {
		return LocalDateTime.parse(strFecha, formatter);
	}

	private static final String REGEX_UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

	public static boolean checkFormatoUuid(String uuid) {
		return uuid.matches(REGEX_UUID);
	}

}
