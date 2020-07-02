package com.fl.springboot.web.app.models.dao;


import org.springframework.data.repository.CrudRepository;

import com.fl.springboot.web.app.models.entity.Cliente;

public interface IClienteDAO extends CrudRepository<Cliente, Long>{
	
}
