package com.w2m.sergiojimenez.retow2msjr.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.w2m.sergiojimenez.retow2msjr.dao.SuperheroeDAO;
import com.w2m.sergiojimenez.retow2msjr.exceptions.ParamNecesarioInexistenteException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.PesoNegativoException;
import com.w2m.sergiojimenez.retow2msjr.model.Superheroe;

@Service
public class GestionarSuperheroesService {

	@Autowired
	private SuperheroeDAO superheroeDAO;

	public void crearSuperheroe(String nombre, Double peso, LocalDateTime fechaNacimiento)
			throws ParamNecesarioInexistenteException, PesoNegativoException {

		Superheroe s = new Superheroe();
		s.setNombre(nombre);
		s.setPeso(peso);
		s.setFechaNacimiento(fechaNacimiento);

		superheroeDAO.save(s);
	}

	public void modificarSuperheroe(Superheroe superheroe) {
		// TODO
	}

	public void eliminarSuperheroe(Superheroe superheroe) {
		// TODO
	}

}
