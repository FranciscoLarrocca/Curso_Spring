package com.fl.springboot.web.app.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fl.springboot.web.app.models.dao.IUsuarioDAO;
import com.fl.springboot.web.app.models.entity.Role;
import com.fl.springboot.web.app.models.entity.Usuario;


@Service("usuarioJPAService")
public class UsuarioJPAService implements UserDetailsService{
	
	private Logger log = LoggerFactory.getLogger(UsuarioJPAService.class); 
	
	@Autowired
	private IUsuarioDAO usuarioDAO;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioDAO.findByUsername(username);		
		if(usuario == null) {
			log.error("ERROR: no existe el usuario: " + username);
			throw new UsernameNotFoundException("Usuario: "+username+ " no existe.");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();		
		for (Role roles: usuario.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(roles.getAuthority()));
		}
		if(authorities.isEmpty()) {
			log.error("ERROR: el usuario: " + username + " NO tiene roles asignados.");
			throw new UsernameNotFoundException("Usuario: "+ username + " NO tiene roles asignados.");
		}
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

}
