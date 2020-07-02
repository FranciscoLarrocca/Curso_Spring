package com.fl.springboot.web.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fl.springboot.web.app.models.entity.Cliente;
import com.fl.springboot.web.app.models.entity.Factura;
import com.fl.springboot.web.app.models.entity.ItemFactura;
import com.fl.springboot.web.app.models.entity.Producto;
import com.fl.springboot.web.app.service.IClienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
@Secured("ROLE_ADMIN")
public class FacturaController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IClienteService service;
	
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value="clienteId") Long id, Model model, RedirectAttributes flash) {
		Cliente cliente = service.findOne(id);
		
		if(cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la BD");
			return "redirect:/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.addAttribute("titulo","Panel de facturación:");
		model.addAttribute("factura", factura);
		return "factura/form";
		
	}
	
	@PostMapping("/form")
	public String guardar(@Valid Factura factura, BindingResult result, Model model,
										   @RequestParam(name="item_id[]", required=false) Long[] itemId,
										   @RequestParam(name="cantidad[]", required=false) Integer[] cantidad,
										   RedirectAttributes flash, SessionStatus status) {
		// Valid activa la validacion en el objeto factura / BindingResult permite
		// comprobar si exisitieron errores en la validacion
		
		if(result.hasErrors()) {
			log.error("Result = " + result.getErrorCount() + "->" + result.getFieldError().toString());
			model.addAttribute("titulo", "Panel de facturación:");
			return "factura/form";
		}
		if(itemId == null || itemId.length == 0) {
			log.error("itemID = " + itemId);
			model.addAttribute("titulo", "Panel de facturación:");
			model.addAttribute("error", "Error: La factura que ha creado, esta vacia.");
			return "factura/form";
		}
		
		for (int i = 0; i < itemId.length; i++) {
			
			Producto producto = service.findProductoById(itemId[i]);
		
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			
			factura.addItem(linea);
			
			log.info("ID: " + itemId[i].toString() + " | Cantidad: " + cantidad[i].toString());
		}
		
		service.saveFactura(factura);
		status.setComplete();
		
		flash.addFlashAttribute("success", "Factura creada con exito.");
		
		return "redirect:/ver/" + factura.getCliente().getId();
	}
	
	@GetMapping("/ver/{id}")
	public String verFactura(@PathVariable Long id, Model model, RedirectAttributes flash) {
		
		Factura factura = service.findFacturaById(id);
		
		if(factura == null) {
			flash.addAttribute("error","La factura no existe en la base de datos.");
			return "redirect:/listar";
		}

		model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));
		model.addAttribute("factura", factura);
		
		return "factura/ver";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminarFactura(@PathVariable Long id, RedirectAttributes flash) {
		Factura factura = service.findFacturaById(id);
		
		if(factura != null) {
			service.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura eliminida con exito.");
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		flash.addFlashAttribute("error", "La factura no existe en la BD");
		return "redirect:/listar";
		
	}
	
	
	//Metodo handler para el autocomplete de productos:
	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return service.findByNombre(term);
	}
	
}
