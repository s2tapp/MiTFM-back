package com.ginoquin.springboot.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "peticion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Peticion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, precision = 9, scale = 0)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", length = 2)
	private EstadoEnum estado;

	@Enumerated(EnumType.STRING)
	@Column(name = "resultado", nullable = false, length = 2)
	private ResultadoEnum resultado;
	
	@Column(name = "transcripción", length = 4096)
	private String transcripción;
		
	@Column(name = "tiempoProceso", precision = 19, scale = 0)
	private Long tiempoProceso;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha", nullable = false, length = 7)
	private Date fecha;

}
