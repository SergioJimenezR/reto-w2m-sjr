package com.w2m.sergiojimenez.retow2msjr.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
public class Superheroe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9110153445062441281L;

	@Id
	@NotNull
	@Column(length = 36)
	private String uuid;

	@NotNull
	@Column(unique = true)
	private String nombre;

	@Positive
	private double peso;

	private Date fechaNacimiento;

	public Superheroe() {
		setUuid(UUID.randomUUID().toString());
	}

	public Superheroe(@NotNull String nombre, @Positive double peso, Date fechaNacimiento) {
		this();
		setNombre(nombre);
		setPeso(peso);
		setFechaNacimiento(fechaNacimiento);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	@Override
	public String toString() {
		return "Superheroe [uuid=" + uuid + ", nombre=" + nombre + ", peso=" + peso + ", fechaNacimiento="
				+ fechaNacimiento + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(fechaNacimiento, nombre, peso, uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Superheroe other = (Superheroe) obj;
		return Objects.equals(fechaNacimiento, other.fechaNacimiento) && Objects.equals(nombre, other.nombre)
				&& Double.doubleToLongBits(peso) == Double.doubleToLongBits(other.peso)
				&& Objects.equals(uuid, other.uuid);
	}

}