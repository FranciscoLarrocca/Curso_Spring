package com.fl.springboot.web.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.fl.springboot.web.app.models.entity.Cliente;

public interface IClienteDAO extends PagingAndSortingRepository<Cliente, Long>{
	
}
