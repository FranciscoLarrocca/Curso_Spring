package com.fl.springboot.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringBootDataJpaPractica01Application implements CommandLineRunner{

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaPractica01Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String password_admin="admin";
		String password_user="user";
		
		String bcryptPasswordAdmin = passwordEncoder.encode(password_admin);
		String bcryptPasswordUser = passwordEncoder.encode(password_user);
		
		System.out.println("Password encriptado ADMIN: " + bcryptPasswordAdmin);
		System.out.println("Password encriptado USER: " + bcryptPasswordUser);
	}

}
