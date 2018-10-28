package com.karen.moneylizer.core.entity.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;

@Entity
@Table(name = "user_entity")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 2059380022311094257L;

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@Id
	private String id;

	private String name;

	@OneToOne
	@MapsId
	private UserAccountEntity userAccount;

}