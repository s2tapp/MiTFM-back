package com.ginoquin.springboot.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ginoquin.springboot.app.models.entity.Peticion;

public interface IPeticionDao extends JpaRepository<Peticion, Long> {
	
}
