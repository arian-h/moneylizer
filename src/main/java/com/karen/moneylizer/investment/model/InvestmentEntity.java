package com.karen.moneylizer.investment.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.karen.moneylizer.utils.RandomAlphanumericIdGenerator;

@Entity
@Table(name = "investment_entity")
public class InvestmentEntity implements Serializable {

	private static final long serialVersionUID = -2436361551434327973L;

	@Id
	@GeneratedValue(generator = RandomAlphanumericIdGenerator.generatorName)
	@GenericGenerator(name = RandomAlphanumericIdGenerator.generatorName, strategy = "com.karen.moneylizer.utils.RandomAlphanumericIdGenerator")
	private String id;

}
