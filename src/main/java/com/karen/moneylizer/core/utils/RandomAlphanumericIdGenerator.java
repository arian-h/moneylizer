package com.karen.moneylizer.core.utils;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class RandomAlphanumericIdGenerator implements IdentifierGenerator,
		Configurable {

	public final static String generatorName = "RandomAlphanumericIdGenerator";
	private final static int RANDOM_STRING_LENGTH = 10;
	@Override
	public Serializable generate(SharedSessionContractImplementor session,
			Object object) throws HibernateException {
		return RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
	}

	@Override
	public void configure(Type type, Properties params,
			ServiceRegistry serviceRegistry) throws MappingException {		
	}

}
