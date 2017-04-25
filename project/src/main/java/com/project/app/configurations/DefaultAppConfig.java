package com.project.app.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.project.app.repositories.UserRepository;
import com.project.app.repositories.UserRoleRepository;
import com.project.app.util.InjectionHelper;

/**
 * Bean Repository for common beans
 * 
 * @author Pavan
 *
 */
@Configuration
public class DefaultAppConfig {

	public static Logger logger = LoggerFactory
			.getLogger(DefaultAppConfig.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	

	/**
	 * {@link InjectionHelper} bean initializing
	 * 
	 * @return
	 */
	@Bean
	@Order(2)
	public InjectionHelper autowireHelper() {
		return new InjectionHelper();
	}


	
}
