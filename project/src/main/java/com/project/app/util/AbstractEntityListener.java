package com.project.app.util;

import java.text.ParseException;
import java.util.UUID;

import javax.persistence.PrePersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.app.entities.AbstractEntity;

public class AbstractEntityListener {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AuthUtilService authUtilService;
/**
 * 
 * @param abstractEntity
 * @throws ParseException
 */
	@PrePersist
	public void setProperty(AbstractEntity abstractEntity) throws ParseException {

		InjectionHelper.autowire(this, this.authUtilService);
		if (abstractEntity.getId()==null)
			abstractEntity.setId(UUID.randomUUID().toString());
	
	}
}
