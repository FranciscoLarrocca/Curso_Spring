package com.fl.springboot.web.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fl.springboot.web.app.models.entity.Cliente;


public interface IClienteService {
	public List<Cliente> findAll();
	public void save(Cliente cliente);
	public Cliente findOne(Long id);
	public void delete(Long id);
}
