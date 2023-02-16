package com.w2m.sergiojimenez.retow2msjr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.w2m.sergiojimenez.retow2msjr.dao.SuperheroeDAO;
import com.w2m.sergiojimenez.retow2msjr.model.Superheroe;

@Service
public class SuperheroeService {

	@Autowired
	private SuperheroeDAO superheroeDAO;

	public List<Superheroe> obtenerTodos() {
		return null; // TODO
	}

	public Superheroe buscarPorId(String id) {
		return null; // TODO
	}

	public List<Superheroe> obtenerLosQueContenganNombre(String patron) {
		return null; // TODO;
	}

	public void crearSuperheroe(Superheroe superheroe) {
		// TODO
	}

	public void modificarSuperheroe(Superheroe superheroe) {
		// TODO
	}

	public void eliminarSuperheroe(Superheroe superheroe) {
		// TODO
	}

}
