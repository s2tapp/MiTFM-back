package com.ginoquin.springboot.app.models.entity;

public enum EstadoEnum {

	P("P", "Procesado"), PP("PP", "Pendiente de procesamiento");

	private final String codigo;
	private final String name;

	EstadoEnum(String codigo, String name) {
		this.codigo = codigo;
		this.name = name;
	}

	public String geCodigo() {
		return codigo;
	}

	public String getName() {
		return name;
	}
}
