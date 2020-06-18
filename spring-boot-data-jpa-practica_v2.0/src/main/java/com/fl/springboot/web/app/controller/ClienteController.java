package com.fl.springboot.web.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fl.springboot.web.app.models.entity.Cliente;
import com.fl.springboot.web.app.service.IClienteService;

@Controller("/")
public class ClienteController {

	@Autowired
	//@Qualifier("clienteDAO-JPA") // Indica el nombre de la implementacion a inyectar. (Seleccionar el Bean/componente concreto).
	private IClienteService service;

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", service.findAll());
		return "listar";
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String crear(Model model) {
		model.addAttribute("titulo", "Formulario de Cliente");
		
		Cliente cliente = new Cliente();
		
		model.addAttribute("cliente", cliente);
				
		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, RedirectAttributes flash, Model model) { //@Valid es para implementar la validacion especificada en la clase cliente.| BindingResult siempre va seguido del objeto a validar
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		} else {
			
			String mensajeFlash = (cliente.getId() > 0)? "Cliente editado con exito" : "Cliente creado con exito!";
			
			service.save(cliente);
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:/listar";
		}
	}
	
	@RequestMapping(value="/form/{id}")
	public String editar(Model model, @PathVariable(value="id") long id, RedirectAttributes flash) {
		Cliente cliente = null;
		
		if (id > 0) {
			cliente = service.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la Base de Datos");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser menor que 0");
			return "redirect:/listar";
		}
				
		model.addAttribute("titulo", "Editar Cliente");	
		model.addAttribute("cliente", cliente);
				
		return "form";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") long id, RedirectAttributes flash) {
		if(id > 0) {
		service.delete(id);
		flash.addFlashAttribute("success", "Cliente eliminado con exito!");
		}
		return "redirect:/listar";
	}
}
