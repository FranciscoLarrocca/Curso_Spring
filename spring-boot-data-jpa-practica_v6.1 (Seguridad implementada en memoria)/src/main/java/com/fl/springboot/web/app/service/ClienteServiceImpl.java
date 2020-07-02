package com.fl.springboot.web.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fl.springboot.web.app.models.dao.IClienteDAO;
import com.fl.springboot.web.app.models.dao.IFacturaDAO;
import com.fl.springboot.web.app.models.dao.IProductoDAO;
import com.fl.springboot.web.app.models.entity.Cliente;
import com.fl.springboot.web.app.models.entity.Factura;
import com.fl.springboot.web.app.models.entity.Producto;

@Service //Esta basado en el patron de diseño facade.
public class ClienteServiceImpl implements IClienteService {
	
	@Autowired	
	private IClienteDAO dao;
	
	@Autowired
	private IProductoDAO productoDAO;
	
	@Autowired
	private IFacturaDAO facturaDAO;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) dao.findAll();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		dao.save(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		return dao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		dao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return dao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		return productoDAO.findByNombre(term);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
		return productoDAO.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDAO.save(factura);		
	}

	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		return facturaDAO.findById(id).orElse(null);
	}

	@Override
	public void deleteFactura(Long id) {
		facturaDAO.deleteById(id);		
	}
	
}
