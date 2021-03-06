package com.fl.springboot.web.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fl.springboot.web.app.models.entity.Cliente;
import com.fl.springboot.web.app.models.entity.Factura;
import com.fl.springboot.web.app.models.entity.Producto;


public interface IClienteService {
	public List<Cliente> findAll();
	public Page<Cliente> findAll(Pageable pageable);
	public void save(Cliente cliente);
	public Cliente findOne(Long id);
	public void delete(Long id);
	
	//Producto
	public List<Producto> findByNombre(String term); 
	public Producto findProductoById(Long id);

	//Factura
	public void saveFactura(Factura factura);
	public Factura findFacturaById(Long id);
	public void deleteFactura(Long id);
}
