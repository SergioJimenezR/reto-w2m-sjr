package com.w2m.sergiojimenez.retow2msjr.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.w2m.sergiojimenez.retow2msjr.model.Superheroe;
import com.w2m.sergiojimenez.retow2msjr.service.SuperheroeService;

@RestController
@RequestMapping("/superheroes")
public class SuperheroeController {

	@Autowired
	private SuperheroeService superheroeService;

	// http://localhost:8080/superheroes/getAll
	@GetMapping("/getAll")
	public List<Superheroe> getAll() {
		return null; // TODO
	}

	// http://localhost:8080/superheroes/getBy?id=...
	@GetMapping("/getBy")
	public Superheroe getById(@RequestParam String id) {
		return null; // TODO
	}

	// http://localhost:8080/superheroes/getAllContaining/man
	@GetMapping("getAllContaining/{patron}")
	public List<Superheroe> getAllContaining(@PathVariable String patron) {
		return null; // TODO
	}

	// http://localhost:8080/superheroes/nuevoSuperheroe
	@PostMapping("/nuevoSuperheroe")
	public void nuevoSuperheroe(@RequestBody Superheroe superheroe) {
		// TODO
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