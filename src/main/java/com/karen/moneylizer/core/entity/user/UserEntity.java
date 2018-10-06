package com.karen.moneylizer.core.entity.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;

@Entity
@Table(name = "user_entity")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 2059380022311094257L;

	@Id
	private String id;

	@OneToOne
	@MapsId
	private UserAccountEntity userAccount;

	public UserEntity() {}

	public UserAccountEntity getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccountEntity userAccount) {
		this.userAccount = userAccount;
	}

}
