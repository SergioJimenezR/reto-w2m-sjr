package com.w2m.sergiojimenez.retow2msjr.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.w2m.sergiojimenez.retow2msjr.dao.SuperheroeDAO;
import com.w2m.sergiojimenez.retow2msjr.exceptions.SuperheroeInexistenteException;
import com.w2m.sergiojimenez.retow2msjr.model.Superheroe;

@Service
public class BuscarSuperheroesService {

	@Autowired
	private SuperheroeDAO superheroeDAO;

	public List<Superheroe> obtenerTodos() {
		return superheroeDAO.findAll();
	}

	public Superheroe buscarPorUuid(String uuid) throws SuperheroeInexistenteException {
		Optional<Superheroe> optS = superheroeDAO.findByUuid(uuid);
		if (optS.isEmpty())
			throw new SuperheroeInexistenteException(HttpStatus.NOT_FOUND,
					"No existe ningún superheroe con el identificador '" + uuid + "'.");
		return optS.get();
	}

	public List<Superheroe> obtenerLosQueContenganNombre(String patron) {
		return superheroeDAO.findAllByNombreContaining(patron);
	}

}
