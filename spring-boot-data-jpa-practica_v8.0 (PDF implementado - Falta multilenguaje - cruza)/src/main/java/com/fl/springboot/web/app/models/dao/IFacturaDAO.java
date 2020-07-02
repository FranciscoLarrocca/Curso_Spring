package com.fl.springboot.web.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.fl.springboot.web.app.models.entity.Factura;

public interface IFacturaDAO extends CrudRepository<Factura, Long> {

}
