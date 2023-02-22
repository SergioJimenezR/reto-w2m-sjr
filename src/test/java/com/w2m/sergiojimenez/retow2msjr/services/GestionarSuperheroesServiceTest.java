package com.w2m.sergiojimenez.retow2msjr.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.w2m.sergiojimenez.retow2msjr.dao.SuperheroeDAO;
import com.w2m.sergiojimenez.retow2msjr.exceptions.ParamNecesarioInexistenteException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.PesoNegativoException;
import com.w2m.sergiojimenez.retow2msjr.model.Superheroe;
import com.w2m.sergiojimenez.retow2msjr.utils.Utilidades;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS) // Necesario para el @BeforeAll no static.
class GestionarSuperheroesServiceTest {

	@Autowired
	private GestionarSuperheroesService service;

	@Autowired
	private SuperheroeDAO dao;

	private static List<Superheroe> datosIniciales;

	@BeforeAll
	void pre() {
		/*
		 * Se almacenarán los datos iniciales de prueba de test/resources/data.sql en
		 * una lista estática para ir utilizándolos eventualmente.
		 */
		datosIniciales = dao.findAll();
	}

	@AfterAll
	void post() {
		/*
		 * Reverso del pre().
		 */
		dao.deleteAll();
		dao.saveAll(datosIniciales);
	}

	@BeforeEach
	void each() {
		dao.deleteAll();
	}

	@Test
	void testCrearSuperheroe() throws ParamNecesarioInexistenteException, PesoNegativoException {

		assertEquals(0, dao.findAll().size());

		Superheroe s = service.crearSuperheroe("Pepe", 5.0, LocalDateTime.now());
		assertEquals(1, dao.findAll().size());

		assertTrue(dao.findByUuid(s.getUuid()).get().equals(s));
	}

	@Test
	void testModificarSuperheroe() throws ParamNecesarioInexistenteException, PesoNegativoException {

		String anteriorNombre = "Pepe";
		assertEquals(false, dao.existsByNombre(anteriorNombre)); // Nombre libre
		double anteriorPeso = 5.0;
		LocalDateTime anteriorFechaNacimiento = LocalDateTime.now();

		String posteriorNombre = "Pedro";
		assertEquals(false, dao.existsByNombre(posteriorNombre)); // Nombre libre
		double posteriorPeso = 10.0;
		LocalDateTime posteriorFechaNacimiento = LocalDateTime.now();

		Superheroe sOriginal = service.crearSuperheroe(anteriorNombre, anteriorPeso, anteriorFechaNacimiento); // Creación.

		service.modificarSuperheroe(sOriginal, posteriorNombre, posteriorPeso, posteriorFechaNacimiento); // Modificación.

		// Contraste con datos cambiados:
		Superheroe sModificado = dao.findByUuid(sOriginal.getUuid()).get();
		assertEquals(sModificado.getNombre(), posteriorNombre);
		assertEquals(sModificado.getPeso(), posteriorPeso);
		assertEquals(Utilidades.parseLocalDateTimeToString(sModificado.getFechaNacimiento()),
				Utilidades.parseLocalDateTimeToString(posteriorFechaNacimiento));
	}

	@Test
	void testEliminarSuperheroe() throws ParamNecesarioInexistenteException, PesoNegativoException {

		Superheroe s = service.crearSuperheroe("Jose", 15.0, LocalDateTime.now());
		assertTrue(dao.existsByUuid(s.getUuid()));

		service.eliminarSuperheroe(s.getUuid());
		assertFalse(dao.existsByUuid(s.getUuid()));

	}

}
