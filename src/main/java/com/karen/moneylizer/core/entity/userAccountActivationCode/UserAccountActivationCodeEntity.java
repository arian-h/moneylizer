package com.karen.moneylizer.core.entity.userAccountActivationCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.RandomStringUtils;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;

@Getter
@Setter
@Entity
@Table(name="user_account_activation_code_entity")
public class UserAccountActivationCodeEntity {

	private final static int ACTIVATION_CODE_LENGTH = 5;

	@Id
	private String userId;

	@OneToOne
	@MapsId
	private UserAccountEntity userAccount;

	private String activationCode;

	public UserAccountActivationCodeEntity() {
		this.activationCode = RandomStringUtils.randomAlphanumeric(ACTIVATION_CODE_LENGTH).toUpperCase();
	}

	public UserAccountActivationCodeEntity(String activationCode) {
		this.activationCode = activationCode;
	}
}
