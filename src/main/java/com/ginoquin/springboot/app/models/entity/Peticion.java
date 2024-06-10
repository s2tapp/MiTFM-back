package com.ginoquin.springboot.app.models.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
	@Column(name = "estado", nullable = false, length = 2)
	private EstadoEnum estado;

	@Enumerated(EnumType.STRING)
	@Column(name = "resultado", length = 2)
	private ResultadoEnum resultado;
	
	@Column(name="nombreFichero", nullable = false, length = 64)
	private String nombreFichero;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tecnologia", nullable = false, length = 10)
	private TechnologyEnum technology;
	
	@Column(name = "transcripcion", length = 4096)
	private String transcripcion;
		
	@Column(name = "tiempoProceso", precision = 6, scale = 3)
	private BigDecimal tiempoProceso;
	
	@Column(name = "confianza", precision = 4, scale = 3)
	private BigDecimal confianza;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha", nullable = false, length = 7)
	private Date fecha;

}
