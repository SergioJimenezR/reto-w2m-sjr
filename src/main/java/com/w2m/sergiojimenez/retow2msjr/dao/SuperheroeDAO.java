package com.w2m.sergiojimenez.retow2msjr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.w2m.sergiojimenez.retow2msjr.model.Superheroe;

public interface SuperheroeDAO extends JpaRepository<Superheroe, String> {

	List<Superheroe> findAllByNombreContaining(String patron);

}
