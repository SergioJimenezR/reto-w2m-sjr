package com.w2m.sergiojimenez.retow2msjr.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;

import com.w2m.sergiojimenez.retow2msjr.exceptions.ParamNecesarioInexistenteException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.PesoNegativoException;
import com.w2m.sergiojimenez.retow2msjr.exceptions.FormatoUUIDInvalidoException;
import com.w2m.sergiojimenez.retow2msjr.utils.Utilidades;

@Entity
public class Superheroe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9110153445062441281L;

	@Id
	@NotNull
	@Column(length = 36)
	private String uuid; // PK.

	@NotNull
	@Column(unique = true)
	private String nombre; // Unique.

	@Positive
	private Double peso; // Mayor que 0.

	private LocalDateTime fechaNacimiento;

	public Superheroe() {
		this.uuid = UUID.randomUUID().toString();
	}

	public Superheroe(@NotNull String nombre, @Positive Double peso, LocalDateTime fechaNacimiento)
			throws ParamNecesarioInexistenteException, PesoNegativoException {
		this();
		setNombre(nombre);
		setPeso(peso);
		setFechaNacimiento(fechaNacimiento);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) throws ParamNecesarioInexistenteException, FormatoUUIDInvalidoException {
		if (uuid == null) // @NotNull.
			throw new ParamNecesarioInexistenteException(HttpStatus.EXPECTATION_FAILED, "El UUID es necesario.");
		else if (!Utilidades.checkFormatoUuid(uuid)) // Regex.
			throw new FormatoUUIDInvalidoException(HttpStatus.FORBIDDEN,
					"El UUID proporcionado '" + uuid + "' no cumple con el formato.");
		this.uuid = uuid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) throws ParamNecesarioInexistenteException {
		if (nombre == null) // @NotNull.
			throw new ParamNecesarioInexistenteException(HttpStatus.EXPECTATION_FAILED, "El nombre es necesario.");
		this.nombre = nombre;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) throws PesoNegativoException {
		if (peso <= 0) // @Positive.
			throw new PesoNegativoException(HttpStatus.FORBIDDEN, "El peso indicado '" + peso + "' es inválido.");
		this.peso = peso;
	}

	public LocalDateTime getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDateTime fechaNacimiento) {
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
		return Objects.equals(nombre, other.nombre) && Objects.equals(peso, other.peso)
				&& Objects.equals(uuid, other.uuid) && Utilidades.parseLocalDateTimeToString(fechaNacimiento)
						.equals(Utilidades.parseLocalDateTimeToString(other.fechaNacimiento));
	}

}