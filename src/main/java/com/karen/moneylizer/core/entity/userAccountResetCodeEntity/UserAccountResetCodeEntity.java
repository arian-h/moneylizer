package com.karen.moneylizer.core.entity.userAccountResetCodeEntity;

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
@Table(name="user_account_reset_code_entity")
public class UserAccountResetCodeEntity {

	private final static int RESET_CODE_LENGTH = 20;

	@Id
	private String userId;

	@OneToOne
	@MapsId
	private UserAccountEntity userAccount;

	private String resetCode;

	public UserAccountResetCodeEntity() {
		this.resetCode = generateResetCode();
	}

	public UserAccountResetCodeEntity(String resetCode) {
		this.resetCode = resetCode;
	}

	public void refreshResetCode() {
		this.resetCode = generateResetCode();
	}

	private String generateResetCode() {
		return RandomStringUtils.randomAlphanumeric(RESET_CODE_LENGTH).toUpperCase();
	}

}
