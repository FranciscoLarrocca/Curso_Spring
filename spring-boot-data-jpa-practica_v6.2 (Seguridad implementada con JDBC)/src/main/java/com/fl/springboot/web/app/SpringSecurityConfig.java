package com.fl.springboot.web.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fl.springboot.web.app.authandler.LoginSuccessHandler;

@EnableGlobalMethodSecurity(securedEnabled = true) //Habilitar anotaciones en los controladores "@Secured"
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoginSuccessHandler successHandler;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private DataSource datasource;
	
	@Autowired
	public void configuerGlobal(AuthenticationManagerBuilder builder) throws Exception{
		
		//Autenticacion con JDBC:
		builder.jdbcAuthentication()
		.dataSource(datasource)
		.passwordEncoder(passwordEncoder).usersByUsernameQuery("select username, password, enabled from users where username=?") //username viene como parametro del formulario de login.
		.authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?"); //Consulta para obtener los roles/authorities.
		
		
		//Se utilizaba inMemory, ahora es con JDBC.
		/*PasswordEncoder encoder = passwordEncoder;
		UserBuilder users = User.builder().passwordEncoder(password -> {return encoder.encode(password);}); //Expresion Lambda.

		builder.inMemoryAuthentication().withUser(users.username("admin").password("admin").roles("ADMIN", "USER"))
										.withUser(users.username("user").password("user").roles("USER"));*/
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
		/*Reemplezar autorizaciones con anotaciones en el controlador */
		/*.antMatchers("/ver/**").hasAnyRole("USER")
		.antMatchers("/uploads/**").hasAnyRole("USER")
		.antMatchers("/form/**").hasAnyRole("ADMIN")
		.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
		.antMatchers("/factura/**").hasAnyRole("ADMIN")*/
		.anyRequest().authenticated()
		.and().formLogin()
				.successHandler(successHandler)
				.loginPage("/login").permitAll() //Permitir log in para todos los usuarios.
		.and().logout().permitAll() //Permitir log out para todos los usuarios.
		.and().exceptionHandling().accessDeniedPage("/error_403"); 
	
	}
	
	
	
}
