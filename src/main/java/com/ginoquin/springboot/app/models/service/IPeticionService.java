package com.ginoquin.springboot.app.models.service;

import java.util.List;

import com.ginoquin.springboot.app.models.entity.Peticion;

public interface IPeticionService {
	
	public List<Peticion> findAll();
	public Peticion findOne(Long id);
	public void savePeticion(Peticion peticion);

}
