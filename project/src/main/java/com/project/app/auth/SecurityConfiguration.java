package com.project.app.auth;
/*
 *@author deepanjan.mal
 */
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.project.app.entities.User;
import com.project.app.entities.UserRole;
import com.project.app.enums.UserEnable;
import com.project.app.repositories.UserRepository;
import com.project.app.repositories.UserRoleRepository;
import com.project.app.util.CustomUserDetailsService;
import com.project.app.util.InjectionHelper;

/**
 * Security and Authentication using HttpBasic Authentication using
 * UserDetailsService Managing CSRF authentication Encoding password using
 * BCryptEncoder by spring.crypto
 *
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomUserDetailsService userDetailsService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleRepository userRoleRepository;


	@Autowired
	InjectionHelper injectionHelper;

	@Autowired
	DataSource datasource;
	Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${spring.mail.mailFrom.password}")
	private String password;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic().and().authorizeRequests().antMatchers("/public/**").permitAll().antMatchers("/sa/**")
				.hasAuthority("sa").antMatchers("/admin/**").hasAuthority("admin").antMatchers("/user/**")
				.hasAuthority("user").antMatchers("/all/**")
				.access("hasAnyAuthority('user') or hasAnyAuthority('admin')").and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index.html").and()
				.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class).csrf()
				.disable();

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());

	}

	@Autowired
	public void defaultSetup() {

		List<User> u = userRepository.findAll();
		if (u.isEmpty()) {
			
			// Creating a super user
			UserRole roleSuperAdmin = new UserRole();
			User superadmin = new User();

			superadmin.setId(UUID.randomUUID().toString());
			superadmin.setUsername("super");
			superadmin.setName("super user");
			superadmin.setPassword("super");
			superadmin.setEnabled(true);
			superadmin.setStatus(UserEnable.ACTIVE);
			superadmin = userRepository.save(superadmin);
			roleSuperAdmin.setRoleType("sa");
			roleSuperAdmin.setUser(superadmin);
			userRoleRepository.save(roleSuperAdmin);

			// Creating default admin
			User admin = new User();
			UserRole roleAdmin = new UserRole();

			admin.setId(UUID.randomUUID().toString());
			admin.setName("admin");
			admin.setUsername("admin");
			admin.setPassword("admin");
			admin.setStatus(UserEnable.ACTIVE);
			admin.setEnabled(true);
			admin = userRepository.save(admin);
			roleAdmin.setRoleType("admin");
			roleAdmin.setUser(admin);
			userRoleRepository.save(roleAdmin);

			// Creating default user
			User user = new User();
			UserRole roleUser = new UserRole();

			user.setId(UUID.randomUUID().toString());
			user.setUsername("user");
			user.setName("user");
			user.setStatus(UserEnable.ACTIVE);
			user.setEnabled(true);
			user.setPassword("user");
			user = userRepository.save(user);
			roleUser.setRoleType("user");
			roleUser.setUser(user);
			userRoleRepository.save(roleUser);

		}
	}

	@SuppressWarnings("unused")
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("CSRF-TOKEN");
		return repository;
	}

	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() throws Exception {
		BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
		entryPoint.setRealmName("Spring");
		return entryPoint;
	}

	@Bean
	public PasswordEncoder passwordencoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordencoder());
		return authenticationProvider;
	}
}