package com.fl.springboot.web.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fl.springboot.web.app.models.entity.Producto;

public interface IProductoDAO extends CrudRepository<Producto, Long>{
	
	@Query("select p from Producto p where p.nombre like %?1%") //(SELECT a nivel de Objeto != tabla)Seleccionar de Producto cuando se igual al paremetro ?1->"String term".
	public List<Producto> findByNombre(String term);
}
