package com.project.app.util;
/*
 * @author deepanjan
 * 
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.project.app.entities.User;

/**
 * Implementation class for {@link AuditorAware}. The object returned by
 * getCurrentAuditor method will be saved as created_by, last_modified_by in
 * audited entities. Audited entities extend {@link AbstractAuditable} class
 * 
 * @author Pavan
 * 
 *
 */
@EnableJpaAuditing
@Configuration
public class AuditingConfig {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Bean
	public AuditingEntityListener createAuditingListener() {
		return new AuditingEntityListener();
	}

	@Component
	public static class AuditorAwareImpl implements AuditorAware<User> {

		@Override
		public User getCurrentAuditor() {
			Authentication authentication = SecurityContextHolder.getContext()
					.getAuthentication();
			if (authentication == null
					|| authentication instanceof AnonymousAuthenticationToken) {
				return null;
			} else {
				return (User) authentication.getPrincipal();
			}
		}

	}
}
