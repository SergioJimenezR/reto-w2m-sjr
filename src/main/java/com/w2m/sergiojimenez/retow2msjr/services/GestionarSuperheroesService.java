package com.w2m.sergiojimenez.retow2msjr.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.w2m.sergiojimenez.retow2msjr.dao.SuperheroeDAO;
import com.w2m.sergiojimenez.retow2msjr.exceptions.ParamNecesarioInexistenteException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.PesoNegativoException;
import com.w2m.sergiojimenez.retow2msjr.model.Superheroe;

@Service
public class GestionarSuperheroesService {

	@Autowired
	private SuperheroeDAO superheroeDAO;

	public Superheroe crearSuperheroe(String nombre, Double peso, LocalDateTime fechaNacimiento)
			throws ParamNecesarioInexistenteException, PesoNegativoException {

		Superheroe s = new Superheroe();
		s.setNombre(nombre);
		s.setPeso(peso);
		s.setFechaNacimiento(fechaNacimiento);

		superheroeDAO.save(s);
		
		return s;
	}

	public void modificarSuperheroe(Superheroe sOriginal, String nuevoNombre, Double nuevoPeso,
			LocalDateTime nuevaFechaNacimiento) throws ParamNecesarioInexistenteException, PesoNegativoException {

		sOriginal.setNombre(nuevoNombre);
		sOriginal.setPeso(nuevoPeso);
		sOriginal.setFechaNacimiento(nuevaFechaNacimiento);

		superheroeDAO.save(sOriginal);
	}

	@Transactional
	public void eliminarSuperheroe(String uuid) {
		superheroeDAO.deleteByUuid(uuid);
	}

}
