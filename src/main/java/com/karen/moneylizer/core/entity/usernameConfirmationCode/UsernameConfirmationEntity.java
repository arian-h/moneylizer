package com.karen.moneylizer.core.entity.usernameConfirmationCode;

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
@Table(name="user_account_username_confirmation_entity")
public class UsernameConfirmationEntity {

	private final static int CONFIRMATION_CODE_LENGTH = 20;

	@Id
	private String userId;

	@OneToOne
	@MapsId
	private UserAccountEntity userAccount;

	private String confirmationCode;

	public UsernameConfirmationEntity() {
		this.confirmationCode = RandomStringUtils.randomAlphanumeric(CONFIRMATION_CODE_LENGTH).toUpperCase();
	}

	public UsernameConfirmationEntity(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}
}
