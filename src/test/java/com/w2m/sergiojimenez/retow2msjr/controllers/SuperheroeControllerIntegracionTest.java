package com.w2m.sergiojimenez.retow2msjr.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.w2m.sergiojimenez.retow2msjr.dao.SuperheroeDAO;
import com.w2m.sergiojimenez.retow2msjr.model.Superheroe;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS) // Necesario para el @BeforeAll no static.
class SuperheroeControllerIntegracionTest {

	@Autowired
	private MockMvc server;

	@Autowired
	private SuperheroeController controller;

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
	void test1() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/superheroes/getAll");
		this.server.perform(request).andExpect(status().isOk());
	}

	@Test
	void testBuscarSuperheroe() throws Exception { // Completo.

		dao.deleteAll();

		Map<String, Object> jsonOriginal = new HashMap<>();
		jsonOriginal.put("nombre", "Pepe");
		jsonOriginal.put("peso", 50.0);
		jsonOriginal.put("fechaNacimiento", "23-02-2023 10:00:00");

		Superheroe sOriginal = controller.nuevoSuperheroe(jsonOriginal);

		RequestBuilder request = MockMvcRequestBuilders.get("/superheroes/getByUuid?uuid=" + sOriginal.getUuid());
		ResultActions resultActions = server.perform(request);
		MvcResult result = resultActions.andExpect(status().is(200)).andReturn();
		MockHttpServletResponse response = result.getResponse();

		String responseBody = response.getContentAsString();
		JSONObject jsonRecuperado = new JSONObject(responseBody);
		System.out.println(responseBody);

		assertTrue(jsonRecuperado.getString("uuid").equals(sOriginal.getUuid()));
		assertTrue(jsonRecuperado.getString("nombre").equals(sOriginal.getNombre()));
		assertEquals(jsonRecuperado.getDouble("peso"), sOriginal.getPeso());

	}

	@Test
	void testBuscarPorUuidInvalido() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/superheroes/getByUuid?uuid=SINFORMATO");
		this.server.perform(request).andExpect(status().is(403)); // (Código de error especificado en el Controller)
	}

	@Test
	void testBuscarPorUuidInexistente() throws Exception {
		boolean existiaEsteUUIDAleatorio = true;
		do {
			String uuid = UUID.randomUUID().toString(); // UUID generalmente/supuestamente inexistente.
			if (!dao.existsByUuid(uuid)) {

				RequestBuilder request = MockMvcRequestBuilders.get("/superheroes/getByUuid?uuid=" + uuid);
				this.server.perform(request).andExpect(status().is(404)); // NOT_FOUND

				existiaEsteUUIDAleatorio = false;
			}
		} while (existiaEsteUUIDAleatorio); // Ya es casualidad...
	}

}
