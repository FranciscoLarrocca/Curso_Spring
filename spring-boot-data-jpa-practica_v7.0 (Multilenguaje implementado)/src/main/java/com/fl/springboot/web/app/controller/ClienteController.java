package com.fl.springboot.web.app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fl.springboot.web.app.models.entity.Cliente;
import com.fl.springboot.web.app.service.IClienteService;
import com.fl.springboot.web.app.util.paginator.PageRender;

@Controller("/")
@SessionAttributes("cliente")
public class ClienteController {
	
	private final Logger log = LoggerFactory.getLogger(getClass()); 
	
	private final static String UPLOAD_FOLDER = "uploads";
	
	@Autowired
	private IClienteService service;
	
	//Seccion: Multilenguaje:
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, Authentication authentication, Locale locale) {
		
		if(authentication != null) {
			log.info("(ClienteController) Hola usuario: " + authentication.getName());
		}
		
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = service.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);

		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String crear(Model model, Locale locale) {
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo", null, locale));

		Cliente cliente = new Cliente();

		model.addAttribute("cliente", cliente);

		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, RedirectAttributes flash, Model model,
			SessionStatus status, @RequestParam("file") MultipartFile foto, Locale locale) { // @Valid es para implementar la validacion especificada en la clase cliente.| BindingResult siempre va seguido del objeto a validar
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo", null, locale));
			return "form";
		
		} else {
			
			if (!foto.isEmpty()) {
				//Editar foto:
				if (cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
					Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();					
					File archivo = rootPath.toFile();

					//Mostrar ruta:
					log.info("RUTA EDITAR: " + archivo.getAbsolutePath().toString());
					
					if (archivo.exists() && archivo.canRead()) {
							archivo.delete();
					}
				}
				
				//Crear foto:
				String uniqueFileName = UUID.randomUUID().toString() +"_"+ foto.getOriginalFilename();
				//Mostrar ruta:
				log.info("RUTA UNIQUE: " + uniqueFileName);
				Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(uniqueFileName); //resolve() concatena el nombre del archivo uploads/nombre_archivo
				Path rootAbsolutePath = rootPath.toAbsolutePath(); //Se crea la ruta absoluta.
				//Mostrar ruta:
				log.info("RUTA CREAR: " + rootAbsolutePath.toString());
				try {
					Files.copy(foto.getInputStream(), rootAbsolutePath); //Escribe el archivo.					
					flash.addFlashAttribute("info", messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale) + " '" + uniqueFileName + "'");

					cliente.setFoto(uniqueFileName);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			String mensajeFlash = (cliente.getId() > 0) ? messageSource.getMessage("text.cliente.flash.editar.success", null, locale) : messageSource.getMessage("text.cliente.flash.crear.success", null, locale);

			service.save(cliente);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:/listar";
		}

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String editar(Model model, @PathVariable(value = "id") long id, RedirectAttributes flash, Locale locale) {
		Cliente cliente = null;

		if (id > 0) {
			cliente = service.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/listar";
		}
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));
		model.addAttribute("cliente", cliente);

		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") long id, RedirectAttributes flash, Locale locale) {
		if (id > 0) {
			Cliente cliente = service.findOne(id);
			
			flash.addFlashAttribute("success", messageSource.getMessage("text.cliente.flash.eliminar.success", null, locale));	

			//Eliminar Foto:
			Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();			
			File archivo = rootPath.toFile();
			
			//Mostrar ruta:
			log.info("RUTA ELIMINAR: " + archivo.getAbsolutePath().toString());
			if (archivo.exists() && archivo.canRead()) {
				if (archivo.delete()) {
					flash.addFlashAttribute("info", String.format(messageSource.getMessage("text.cliente.flash.foto.eliminar.success", null, locale), cliente.getFoto()));
				}
			}
			
			service.delete(id);
		}
		return "redirect:/listar";
	}

	@Secured("ROLE_USER")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {
		Cliente cliente = service.findOne(id);

		if (cliente == null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.detalle.titulo", null, locale)+": "+ cliente.getNombre() + " " + cliente.getApellido());
		return "ver";
	}
}
