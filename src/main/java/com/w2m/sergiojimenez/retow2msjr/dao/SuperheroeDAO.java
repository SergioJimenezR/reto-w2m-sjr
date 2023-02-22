package com.w2m.sergiojimenez.retow2msjr.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.w2m.sergiojimenez.retow2msjr.model.Superheroe;

public interface SuperheroeDAO extends JpaRepository<Superheroe, String> {

	Optional<Superheroe> findByUuid(String uuid);

	Optional<Superheroe> findByNombre(String nombre);

	List<Superheroe> findAllByNombreContaining(String patron);

	boolean existsByUuid(String uuid);

	boolean existsByNombre(String nombre);

}
