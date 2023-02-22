package com.w2m.sergiojimenez.retow2msjr.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.w2m.sergiojimenez.retow2msjr.annotations.MedicionTiempoEjecucion;
import com.w2m.sergiojimenez.retow2msjr.dao.SuperheroeDAO;
import com.w2m.sergiojimenez.retow2msjr.exceptions.ExceptionHTTPPolimorfica;
import com.w2m.sergiojimenez.retow2msjr.exceptions.FormatoUUIDInvalidoException;
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
public class SuperheroeController {

	@Autowired
	private BuscarSuperheroesService buscarSuperheroesService;

	@Autowired
	private GestionarSuperheroesService gestionarSuperheroesService;

	@Autowired
	private SuperheroeDAO superheroeDAO;

	/**
	 * Método endpoint encargado de llevar a cabo la petición <<get>> para devolver
	 * todos los súperhéroes encontrados en la base de datos. No requerirá de
	 * parámetros de entrada, y devolverá la lista aunque se pueda encontrar vacía.
	 * 
	 * http://localhost:8080/superheroes/getAll
	 *
	 * @return Lista de superheroes.
	 */
	@GetMapping("/getAll")
	@MedicionTiempoEjecucion
	public List<Superheroe> getAll() {

		return buscarSuperheroesService.obtenerTodos();
	}

	/**
	 * Método endpoint encargado de llevar a cabo la petición <<get>> para buscar a
	 * un superheroe según su identificador único (UUID). Requerirá de
	 * introducírsele el UUID a buscar, como RequestParam en el enlace URL de la
	 * petición. Contiene cuestiones de eficiencia. Si no existe ningún superhéroe
	 * con tal UUID, provoca un error 404 - NOT FOUND.
	 * 
	 * http://localhost:8080/superheroes/getBy?id=...
	 * 
	 * @param UUID del superhéroe a buscar.
	 * @return Superhéroe encontrado.
	 * @throws FormatoUUIDInvalidoException   en caso de formatear mal el UUID.
	 * @throws SuperheroeInexistenteException en caso de no existir superheroe.
	 */
	@GetMapping("/getBy")
	@MedicionTiempoEjecucion
	public Superheroe getByUuid(@RequestParam String uuid) throws ExceptionHTTPPolimorfica {

		if (!Utilidades.checkFormatoUuid(uuid)) // Eficiencia - Regex.
			/*
			 * Aunque no sería necesario hacer esta comprobación, se realiza por eficiencia.
			 * Si el id es inválido per se, nos ahorramos el coste de hacer la consulta.
			 */
			throw new FormatoUUIDInvalidoException(HttpStatus.FORBIDDEN,
					"El UUID proporcionado '" + uuid + "' no cumple con el formato.");

		return buscarSuperheroesService.buscarPorUuid(uuid);
	}

	/**
	 * Método endpoint encargado de llevar a cabo a petición <<get>> para buscar a
	 * los superhéroes que contengan en su nombre la subcadena dada. Requerirá de
	 * introducírsele dicha subcadena por PathVariable en la URL de la petición.
	 * Devolverá una lista de las instancias de la clase Superheroe que incluyan tal
	 * nombre, almacenadas en la base de datos, aunque se pueda encontrar vacía.
	 * 
	 * http://localhost:8080/superheroes/getAllContaining/man
	 * 
	 * @param Subcadena contenida en los nombres.
	 * @return Lista de superheroes que contengan en su nombre tal subcadena.
	 */
	@GetMapping("getAllContaining/{patron}")
	@MedicionTiempoEjecucion
	public List<Superheroe> getAllContaining(@PathVariable String subcadena) {

		return buscarSuperheroesService.obtenerLosQueContenganNombre(subcadena);
	}

	/**
	 * Método endpoint encargado de registrar un nuevo superhéroe a partir de los
	 * datos pasados por parámetro de entrada en la petición <<post>>. Dichos datos
	 * vendrán dados en formato JSON, uno referido a cada atributo de la clase
	 * Superheroe salvo el UUID, el cual se genera en backend. Además de crearlo,
	 * también devuelve dicha instancia recién creada.
	 * 
	 * Se llevan a cabo distintos controles de Seguridad de la API como: revisar que
	 * no exista ya un Superheroe con el nombre propuesto (al tratarse de Unique), u
	 * otros controles en las anotaciones de validación y setters de la clase como
	 * que el atributo peso deba ser positivo, o que el nombre sea obligatorio de
	 * introducir (Not Null).
	 * 
	 * http://localhost:8080/superheroes/nuevoSuperheroe
	 * 
	 * @param info Mapa JSON que contiene los valores de los atributos de la
	 *             instancia a crear.
	 * @throws NombreRepetidoBBDDException        en caso de ya existir un
	 *                                            superheroe con este nombre
	 *                                            (Unique).
	 * @throws ParamNecesarioInexistenteException en caso de no indicarse un nombre
	 *                                            (Not Null).
	 * @throws PesoNegativoException              en caso de indicarse un peso
	 *                                            inválido (no positivo).
	 */
	@PostMapping("/nuevoSuperheroe")
	@MedicionTiempoEjecucion
	public Superheroe nuevoSuperheroe(@RequestBody Map<String, Object> info) throws ExceptionHTTPPolimorfica {

		JSONObject jso = new JSONObject(info);

		String nombre = jso.optString("nombre");
		if (superheroeDAO.existsByNombre(nombre)) // Unique.
			throw new NombreRepetidoBBDDException(HttpStatus.CONFLICT,
					"Ya existe un superheroe con el nombre " + nombre + ".");

		Double peso = jso.optDouble("peso");

		LocalDateTime fechaNacimiento = Utilidades.parseStringToLocalDateTime(jso.optString("fechaNacimiento"));
		/* Según el formato del String de la fecha indicado en clase Utilidades. */

		return gestionarSuperheroesService.crearSuperheroe(nombre, peso, fechaNacimiento);
	}

	/**
	 * Método endpoint para posibilitar la modificación de la información de un
	 * superhéroe según la presente petición <<put>>. Más concretamente, se pasará
	 * por parámetro de entrada el identificador único UUID del superhéroe que se
	 * quiere modificar, y los demás valores nuevos contenidos en un Mapa JSON.
	 * 
	 * Se llevarán numerosos controles como que: que el superhéroe que se quiere
	 * modificar debiera existir previamente en la base de datos; o que si se quiere
	 * cambiar el nombre (al tratarse de Unique) no exista ningún otro superhéroe
	 * con ese nombre.
	 * 
	 * http://localhost:8080/superheroes/modifySuperheroe
	 *
	 * @param uuid   referido al identificador único del superhéroe a modificar.
	 * @param sNuevo referido a la instancia del Superheroe con los valores nuevos.
	 * @throws SuperheroeInexistenteException     en caso de no existir previamente.
	 * @throws NombreRepetidoBBDDException        en caso de existir otro superhéroe
	 *                                            con el nuevo nombre (Unique).
	 * @throws ParamNecesarioInexistenteException en caso de no indicarse nombre
	 *                                            (Not Null).
	 * @throws PesoNegativoException              en caso de indicarse un peso
	 *                                            inválido (no positivo).
	 */
	@PutMapping("/modifySuperheroe/{uuid}")
	@MedicionTiempoEjecucion
	public void modificarSuperheroe(@PathVariable String uuid, @RequestBody Map<String, Object> info)
			throws ExceptionHTTPPolimorfica {

		Optional<Superheroe> optSOriginal = superheroeDAO.findByUuid(uuid);
		if (optSOriginal.isEmpty()) // Si originalmente no existía el superhéroe que se quiere modificar:
			throw new SuperheroeInexistenteException(HttpStatus.EXPECTATION_FAILED,
					"No existía previamente ningún superheroe con identificador '" + uuid + "'.");

		Superheroe sOriginal = optSOriginal.get();

		JSONObject jso = new JSONObject(info);

		String nombreNuevo = jso.optString("nombre");
		if (!nombreNuevo.equals(sOriginal.getNombre()) /* Se quiere modificar el nombre */
				&& superheroeDAO.existsByNombre(nombreNuevo) /* Nombre Unique ya existía por otra parte */)
			throw new NombreRepetidoBBDDException(HttpStatus.CONFLICT, "Ya existe otro superhéroe con este nombre.");

		Double peso = jso.optDouble("peso");

		LocalDateTime fechaNacimiento = Utilidades.parseStringToLocalDateTime(jso.optString("fechaNacimiento"));
		/* Según el formato del String de la fecha indicado en clase Utilidades. */

		gestionarSuperheroesService.modificarSuperheroe(sOriginal, nombreNuevo, peso, fechaNacimiento);
	}

	/**
	 * Método endpoint encargado de manejar las peticiones <<delete>> para eliminar
	 * un superhéroe de la base de datos. Se requerirá introducir el UUID del
	 * superhéroe a eliminar y no devolverá nada.
	 * 
	 * Se estará controlando que el superhéroe en cuestión exista.
	 * 
	 * http://localhost:8080/superheroes/deleteSuperheroe
	 * 
	 * @param uuid referido al identificador del superhéroe a eliminar.
	 * @throws SuperheroeInexistenteException en caso de no existir el superhéroe.
	 */
	@DeleteMapping("/deleteSuperheroe")
	@MedicionTiempoEjecucion
	public void eliminarSuperheroe(@PathVariable String uuid) throws ExceptionHTTPPolimorfica {

		if (!superheroeDAO.existsByUuid(uuid))
			throw new SuperheroeInexistenteException(HttpStatus.NOT_FOUND,
					"No existía ningún superheroe con identificador '" + uuid + "'.");

		gestionarSuperheroesService.eliminarSuperheroe(uuid);
	}

}
