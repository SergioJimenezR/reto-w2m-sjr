package com.w2m.sergiojimenez.retow2msjr.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.w2m.sergiojimenez.retow2msjr.dao.SuperheroeDAO;
import com.w2m.sergiojimenez.retow2msjr.exceptions.FormatoUUIDInvalidoException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.ParamNecesarioInexistenteException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.PesoNegativoException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.SuperheroeInexistenteException;
import com.w2m.sergiojimenez.retow2msjr.model.Superheroe;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS) // Necesario para el @BeforeAll no static.
class BuscarSuperheroesServiceTest {

	@Autowired
	private BuscarSuperheroesService buscarSuperheroesService;

	@Autowired
	private SuperheroeDAO superheroeDAO;

	private static List<Superheroe> datosIniciales;

	@BeforeAll
	void pre() {
		/*
		 * Se almacenarán los datos iniciales de prueba de test/resources/data.sql en
		 * una lista estática para ir utilizándolos eventualmente.
		 */
		datosIniciales = superheroeDAO.findAll();
	}

	@Test
	void testObtenerTodos() throws ParamNecesarioInexistenteException, PesoNegativoException {

		superheroeDAO.deleteAll();
		assertEquals(0, buscarSuperheroesService.obtenerTodos().size());

		superheroeDAO.saveAll(datosIniciales);
		assertEquals(buscarSuperheroesService.obtenerTodos().size(), datosIniciales.size());

		superheroeDAO.save(new Superheroe("Pepe", 5.0, LocalDateTime.now())); // Instancia nueva.
		assertEquals(buscarSuperheroesService.obtenerTodos().size(), datosIniciales.size() + 1);

	}

	@Test
	void testBuscarPorUuid() throws ParamNecesarioInexistenteException, PesoNegativoException,
			FormatoUUIDInvalidoException, SuperheroeInexistenteException {

		superheroeDAO.deleteAll();
		try {
			buscarSuperheroesService.buscarPorUuid("(UUID que no existe)");
		} catch (SuperheroeInexistenteException e) {
			// Manejado: No existe ningún superheroe con tal UUID.
		}

		Superheroe pepe = new Superheroe("Pepe", 5.0, LocalDateTime.now());
		superheroeDAO.save(pepe);
		assertTrue(buscarSuperheroesService.buscarPorUuid(pepe.getUuid()).equals(pepe));

	}

	@Test
	void testObtenerLosQueContenganNombre() throws ParamNecesarioInexistenteException, PesoNegativoException {

		superheroeDAO.deleteAll();
		assertEquals(0, buscarSuperheroesService.obtenerLosQueContenganNombre("(Nombre no contenido)").size());

		int contador = 0;
		for (Superheroe s : datosIniciales)
			if (s.getNombre().contains("man"))
				contador++;

		superheroeDAO.saveAll(datosIniciales); // Incluye Superman, Manolito, Humano, Deadpool, entre otros.
		assertEquals(superheroeDAO.findAllByNombreContaining("man").size(), contador);

	}

}
