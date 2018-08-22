package com.karen.moneylizer.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

import com.karen.moneylizer.utils.RandomAlphanumericIdGenerator;

@Entity
@Table(name = "user_entity")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 2059380022311094257L;

	@NotBlank
	@Id
	@GeneratedValue(generator = RandomAlphanumericIdGenerator.generatorName)
	@GenericGenerator(name = RandomAlphanumericIdGenerator.generatorName, strategy = "com.karen.moneylizer.utils.RandomAlphanumericIdGenerator")
	private String id;

	public UserEntity() {
	}

}
