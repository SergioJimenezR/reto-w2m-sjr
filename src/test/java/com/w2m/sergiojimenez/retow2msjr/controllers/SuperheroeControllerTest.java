package com.w2m.sergiojimenez.retow2msjr.controllers;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.w2m.sergiojimenez.retow2msjr.dao.SuperheroeDAO;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS) // Necesario para el @AfterAll no static.
class SuperheroeControllerTest {

	@Autowired
	private SuperheroeDAO superheroeDAO;

	@BeforeEach
	@AfterAll
	void setup() {
		superheroeDAO.deleteAll();
	}

	@Test
	void testObtenerTodos() {
		// fail("Not yet implemented");
	}

	@Test
	void testBuscarPorId() {
		// fail("Not yet implemented");
	}

	@Test
	void testObtenerLosQueContenganNombre() {
		// fail("Not yet implemented");
	}

	@Test
	void testCrearSuperheroe() {
		// fail("Not yet implemented");
	}

	@Test
	void testModificarSuperheroe() {
		// fail("Not yet implemented");
	}

	@Test
	void testEliminarSuperheroe() {
		// fail("Not yet implemented");
	}

}
