package com.karen.moneylizer.utils;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import com.google.common.collect.ImmutableMap;

public class RandomAlphanumericIdGenerator implements IdentifierGenerator,
		Configurable {

	public static final String generatorName = "myGenerator";
	private String id_prefix;

	static final Map<String, String> ID_PREFIX_MAP = ImmutableMap
			.<String, String> builder()
			.put("com.karen.moneylizer.investment.model.InvestmentEntity",
					"INV_").build();

	@Override
	public Serializable generate(SharedSessionContractImplementor session,
			Object object) throws HibernateException {
		return id_prefix + RandomStringUtils.randomAlphanumeric(10);
	}

	@Override
	public void configure(Type type, Properties params,
			ServiceRegistry serviceRegistry) throws MappingException {
		id_prefix = ID_PREFIX_MAP.get(params.getProperty("entity_name"));
	}

}
