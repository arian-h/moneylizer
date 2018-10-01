package com.karen.moneylizer.core.entity.userAccountActivityCodeEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.RandomStringUtils;

import com.karen.moneylizer.core.entity.useraccount.UserAccountEntity;

@Entity
@Table(name="user_account_activity_code_entity")
public class UserAccountActivityCodeEntity {

	private final static int ACTIVITY_CODE_LENGTH = 6;

	@Id
	private String userId;

	@OneToOne
	@MapsId
	private UserAccountEntity userAccount;

	private String acitivityCode;

	public UserAccountActivityCodeEntity() {
		this.acitivityCode = RandomStringUtils.randomAlphanumeric(ACTIVITY_CODE_LENGTH);
	}

	public UserAccountEntity getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccountEntity userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAcitivityCode() {
		return acitivityCode;
	}
	public void setAcitivityCode(String acitivityCode) {
		this.acitivityCode = acitivityCode;
	}

}
