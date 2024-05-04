package com.ginoquin.springboot.app.models.entity;

public enum ResultadoEnum {

	OK("OK", "OK"), E("E", "ERROR");

	private final String codigo;
	private final String name;

	ResultadoEnum(String codigo, String name) {
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
