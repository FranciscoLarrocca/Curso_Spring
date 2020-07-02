package com.fl.springboot.web.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.fl.springboot.web.app.models.entity.Usuario;

public interface IUsuarioDAO extends CrudRepository<Usuario, Long> {
	
	public Usuario findByUsername(String username);
	
}
