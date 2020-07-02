package com.fl.springboot.web.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fl.springboot.web.app.models.dao.IClienteDAO;
import com.fl.springboot.web.app.models.entity.Cliente;

@Service //Esta basado en el patron de diseño facade.
public class ClienteServiceImpl implements IClienteService {
	
	@Autowired	
	private IClienteDAO dao;
	
	@Override
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return (List<Cliente>) dao.findAll();
	}

	@Override
	public void save(Cliente cliente) {
		// TODO Auto-generated method stub
		dao.save(cliente);
	}

	@Override
	public Cliente findOne(Long id) {
		// TODO Auto-generated method stub
		return dao.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		dao.deleteById(id);
	}
	
}
