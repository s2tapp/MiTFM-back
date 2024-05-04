package com.ginoquin.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ginoquin.springboot.app.models.dao.IPeticionDao;
import com.ginoquin.springboot.app.models.entity.Peticion;

@Service
public class PeticionServiceImpl implements IPeticionService {

	@Autowired
	private IPeticionDao peticionDao;

	@Override
	@Transactional(readOnly = true)
	public List<Peticion> findAll() {
		return this.peticionDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Peticion findOne(Long id) {
		return this.peticionDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void savePeticion(Peticion peticion) {
		this.peticionDao.save(peticion);
	}
	
}
