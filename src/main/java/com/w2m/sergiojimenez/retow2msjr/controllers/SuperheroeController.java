package com.w2m.sergiojimenez.retow2msjr.controllers;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.w2m.sergiojimenez.retow2msjr.dao.SuperheroeDAO;
import com.w2m.sergiojimenez.retow2msjr.exceptions.FormatoUUIDInvalidoException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.NoExistenSuperheroesException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.NombreRepetidoBBDDException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.ParamNecesarioInexistenteException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.PesoNegativoException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.SuperheroeInexistenteException;
import com.w2m.sergiojimenez.retow2msjr.model.Superheroe;
import com.w2m.sergiojimenez.retow2msjr.services.BuscarSuperheroesService;
import com.w2m.sergiojimenez.retow2msjr.services.GestionarSuperheroesService;
import com.w2m.sergiojimenez.retow2msjr.utils.Utilidades;

@RestController
@RequestMapping("/superheroes")
@CrossOrigin(origins = "*") // Apto para admitir peticiones de ciertos origenes (Angular, etc.)
public class SuperheroeController {

	@Autowired
	private BuscarSuperheroesService buscarSuperheroesService;

	@Autowired
	private GestionarSuperheroesService gestionarSuperheroesService;

	@Autowired
	private SuperheroeDAO superheroeDAO;

	@GetMapping("/p1")
	public String p1() throws ParamNecesarioInexistenteException, PesoNegativoException {
		String str = "p1";
		return str;
	}

	// http://localhost:8080/superheroes/getAll
	@GetMapping("/getAll")
	public List<Superheroe> getAll() throws NoExistenSuperheroesException {

		List<Superheroe> lista = buscarSuperheroesService.obtenerTodos();
		if (lista.isEmpty())
			throw new NoExistenSuperheroesException(HttpStatus.NOT_FOUND, "No existe ningún superheroe.");

		return lista;
	}

	// http://localhost:8080/superheroes/getBy?id=...
	@GetMapping("/getBy")
	public Superheroe getByUuid(@RequestParam String uuid)
			throws FormatoUUIDInvalidoException, SuperheroeInexistenteException {

		if (!Utilidades.checkFormatoUuid(uuid)) // Eficiencia - Regex.
			/*
			 * Aunque no sería necesario hacer esta comprobación, se realiza por eficiencia.
			 * Si el id es inválido per se, nos ahorramos el coste de hacer la consulta.
			 */
			throw new FormatoUUIDInvalidoException(HttpStatus.FORBIDDEN,
					"El UUID proporcionado '" + uuid + "' no cumple con el formato.");

		return buscarSuperheroesService.buscarPorUuid(uuid);
	}

	// http://localhost:8080/superheroes/getAllContaining/man
	@GetMapping("getAllContaining/{patron}")
	public List<Superheroe> getAllContaining(@PathVariable String patron) throws NoExistenSuperheroesException {

		List<Superheroe> lista = buscarSuperheroesService.obtenerLosQueContenganNombre(patron);
		if (lista.isEmpty())
			throw new NoExistenSuperheroesException(HttpStatus.NOT_FOUND, "No existen superheroes con este patrón.");

		return lista;
	}

	// http://localhost:8080/superheroes/nuevoSuperheroe
	@PostMapping("/nuevoSuperheroe")
	public void nuevoSuperheroe(@RequestBody Map<String, Object> info) throws NombreRepetidoBBDDException,
			ParamNecesarioInexistenteException, PesoNegativoException, ParseException {

		JSONObject jso = new JSONObject(info);

		String nombre = jso.optString("nombre");
		if (superheroeDAO.existsByNombre(nombre)) // Unique.
			throw new NombreRepetidoBBDDException(HttpStatus.CONFLICT,
					"Ya existe un superheroe con el nombre " + nombre + ".");

		Double peso = jso.optDouble("peso");
		LocalDateTime fechaNacimiento = Utilidades.parseStringToLocalDateTime(jso.optString("fechaNacimiento"));

		gestionarSuperheroesService.crearSuperheroe(nombre, peso, fechaNacimiento);
	}

	// http://localhost:8080/superheroes/modifySuperheroe
	@PutMapping("/modifySuperheroe")
	public void modificarSuperheroe(@RequestBody Superheroe superheroe) {
		// TODO
	}

	// http://localhost:8080/superheroes/deleteSuperheroe
	@DeleteMapping("/deleteSuperheroe")
	public void eliminarSuperheroe(@RequestBody Superheroe superheroe) {
		// TODO
	}

}