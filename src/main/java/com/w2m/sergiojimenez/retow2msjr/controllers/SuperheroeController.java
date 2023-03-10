package com.w2m.sergiojimenez.retow2msjr.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
	 * M??todo endpoint encargado de llevar a cabo la petici??n <<get>> para devolver
	 * todos los s??perh??roes encontrados en la base de datos. No requerir?? de
	 * par??metros de entrada, y devolver?? la lista aunque se pueda encontrar vac??a.
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
	 * M??todo endpoint encargado de llevar a cabo la petici??n <<get>> para buscar a
	 * un superheroe seg??n su identificador ??nico (UUID). Requerir?? de
	 * introduc??rsele el UUID a buscar, como RequestParam en el enlace URL de la
	 * petici??n. Adem??s, utiliza una Cach?? para almacenar el Superheroe del UUID,
	 * mejorando la eficiencia de la aplicaci??n. Contiene adem??s otra cuesti??n de
	 * seguridad/eficiencia. Si no existe ning??n superh??roe con tal UUID, provoca un
	 * error 404 - NOT FOUND.
	 * 
	 * http://localhost:8080/superheroes/getByUuid?uuid=...
	 * 
	 * @param UUID del superh??roe a buscar.
	 * @return Superh??roe encontrado.
	 * @throws FormatoUUIDInvalidoException   en caso de formatear mal el UUID.
	 * @throws SuperheroeInexistenteException en caso de no existir superheroe.
	 */
	@Cacheable("cacheBuscarSuperheroe")
	@GetMapping("/getByUuid")
	@MedicionTiempoEjecucion
	public Superheroe getByUuid(@RequestParam String uuid) throws ExceptionHTTPPolimorfica {

		if (!Utilidades.checkFormatoUuid(uuid)) // Seguridad y Eficiencia - Regex - Evita SQLInjection.
			/*
			 * Eficiencia: Aunque podr??a no considerarse necesario hacer esta comprobaci??n,
			 * se realiza por eficiencia. Si el id es inv??lido per se, nos ahorramos el
			 * coste de hacer la consulta.
			 */
			throw new FormatoUUIDInvalidoException(HttpStatus.FORBIDDEN,
					"El UUID proporcionado '" + uuid + "' no cumple con el formato.");

		return buscarSuperheroesService.buscarPorUuid(uuid);
	}

	/**
	 * M??todo endpoint encargado de llevar a cabo a petici??n <<get>> para buscar a
	 * los superh??roes que contengan en su nombre la subcadena dada. Requerir?? de
	 * introduc??rsele dicha subcadena por PathVariable en la URL de la petici??n.
	 * Devolver?? una lista de las instancias de la clase Superheroe que incluyan tal
	 * nombre, almacenadas en la base de datos, aunque se pueda encontrar vac??a.
	 * 
	 * http://localhost:8080/superheroes/getAllContaining/man
	 * 
	 * @param Subcadena contenida en los nombres.
	 * @return Lista de superheroes que contengan en su nombre tal subcadena.
	 */
	@GetMapping("getAllContaining/{patron}")
	@MedicionTiempoEjecucion
	@Cacheable("cache1")
	public List<Superheroe> getAllContaining(@PathVariable String subcadena) {

		return buscarSuperheroesService.obtenerLosQueContenganNombre(subcadena);
	}

	/**
	 * M??todo endpoint encargado de registrar un nuevo superh??roe a partir de los
	 * datos pasados por par??metro de entrada en la petici??n <<post>>. Dichos datos
	 * vendr??n dados en formato JSON, uno referido a cada atributo de la clase
	 * Superheroe salvo el UUID, el cual se genera en backend. Adem??s de crearlo,
	 * tambi??n devuelve dicha instancia reci??n creada.
	 * 
	 * Se llevan a cabo distintos controles de Seguridad de la API como: revisar que
	 * no exista ya un Superheroe con el nombre propuesto (al tratarse de Unique), u
	 * otros controles en las anotaciones de validaci??n y setters de la clase como
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
	 *                                            inv??lido (no positivo).
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
		/* Seg??n el formato del String de la fecha indicado en clase Utilidades. */

		return gestionarSuperheroesService.crearSuperheroe(nombre, peso, fechaNacimiento);
	}

	/**
	 * M??todo endpoint para posibilitar la modificaci??n de la informaci??n de un
	 * superh??roe seg??n la presente petici??n <<put>>. M??s concretamente, se pasar??
	 * por par??metro de entrada el identificador ??nico UUID del superh??roe que se
	 * quiere modificar, y los dem??s valores nuevos contenidos en un Mapa JSON.
	 * 
	 * Se llevar??n numerosos controles como que: que el superh??roe que se quiere
	 * modificar debiera existir previamente en la base de datos; o que si se quiere
	 * cambiar el nombre (al tratarse de Unique) no exista ning??n otro superh??roe
	 * con ese nombre.
	 * 
	 * http://localhost:8080/superheroes/modifySuperheroe
	 *
	 * @param uuid   referido al identificador ??nico del superh??roe a modificar.
	 * @param sNuevo referido a la instancia del Superheroe con los valores nuevos.
	 * @throws SuperheroeInexistenteException     en caso de no existir previamente.
	 * @throws NombreRepetidoBBDDException        en caso de existir otro superh??roe
	 *                                            con el nuevo nombre (Unique).
	 * @throws ParamNecesarioInexistenteException en caso de no indicarse nombre
	 *                                            (Not Null).
	 * @throws PesoNegativoException              en caso de indicarse un peso
	 *                                            inv??lido (no positivo).
	 */
	@PutMapping("/modifySuperheroe/{uuid}")
	@MedicionTiempoEjecucion
	public void modificarSuperheroe(@PathVariable String uuid, @RequestBody Map<String, Object> info)
			throws ExceptionHTTPPolimorfica {

		Optional<Superheroe> optSOriginal = superheroeDAO.findByUuid(uuid);
		if (optSOriginal.isEmpty()) // Si originalmente no exist??a el superh??roe que se quiere modificar:
			throw new SuperheroeInexistenteException(HttpStatus.EXPECTATION_FAILED,
					"No exist??a previamente ning??n superheroe con identificador '" + uuid + "'.");

		Superheroe sOriginal = optSOriginal.get();

		JSONObject jso = new JSONObject(info);

		String nombreNuevo = jso.optString("nombre");
		if (!nombreNuevo.equals(sOriginal.getNombre()) /* Se quiere modificar el nombre */
				&& superheroeDAO.existsByNombre(nombreNuevo) /* Nombre Unique ya exist??a por otra parte */)
			throw new NombreRepetidoBBDDException(HttpStatus.CONFLICT, "Ya existe otro superh??roe con este nombre.");

		Double peso = jso.optDouble("peso");

		LocalDateTime fechaNacimiento = Utilidades.parseStringToLocalDateTime(jso.optString("fechaNacimiento"));
		/* Seg??n el formato del String de la fecha indicado en clase Utilidades. */

		gestionarSuperheroesService.modificarSuperheroe(sOriginal, nombreNuevo, peso, fechaNacimiento);
	}

	/**
	 * M??todo endpoint encargado de manejar las peticiones <<delete>> para eliminar
	 * un superh??roe de la base de datos. Se requerir?? introducir el UUID del
	 * superh??roe a eliminar y no devolver?? nada.
	 * 
	 * Se estar?? controlando que el superh??roe en cuesti??n exista.
	 * 
	 * http://localhost:8080/superheroes/deleteSuperheroe
	 * 
	 * @param uuid referido al identificador del superh??roe a eliminar.
	 * @throws SuperheroeInexistenteException en caso de no existir el superh??roe.
	 */
	@DeleteMapping("/deleteSuperheroe")
	@MedicionTiempoEjecucion
	public void eliminarSuperheroe(@PathVariable String uuid) throws ExceptionHTTPPolimorfica {

		if (!superheroeDAO.existsByUuid(uuid))
			throw new SuperheroeInexistenteException(HttpStatus.NOT_FOUND,
					"No exist??a ning??n superheroe con identificador '" + uuid + "'.");

		gestionarSuperheroesService.eliminarSuperheroe(uuid);
	}

}
