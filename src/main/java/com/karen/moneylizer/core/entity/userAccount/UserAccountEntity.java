package com.karen.moneylizer.core.entity.userAccount;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.karen.moneylizer.core.entity.user.UserEntity;
import com.karen.moneylizer.core.entity.userAccountActivationCode.UserAccountActivationCodeEntity;
import com.karen.moneylizer.core.entity.userAccountResetCodeEntity.UserAccountResetCodeEntity;
import com.karen.moneylizer.core.utils.RandomAlphanumericIdGenerator;

@Entity
@Table(name="user_account_entity")
@JsonSerialize(using = UserAccountSerializer.class)
public class UserAccountEntity implements UserDetails {

	private static final long serialVersionUID = -1662173405386513224L;
	private final static String ROLE_USER  = "ROLE_USER";
	private final static long EXPIRATION_TIME = 40 * 1000;

	@Id
	@GeneratedValue(generator = RandomAlphanumericIdGenerator.generatorName)
	@GenericGenerator(name = RandomAlphanumericIdGenerator.generatorName, strategy = "com.karen.moneylizer.core.utils.RandomAlphanumericIdGenerator")
	private String id;

	private String username;

	private String password;

	private long createTime;

	@PrimaryKeyJoinColumn
	@OneToOne(mappedBy= "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UserAccountResetCodeEntity resetCode;

	@PrimaryKeyJoinColumn
	@OneToOne(mappedBy= "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UserEntity user;

	@PrimaryKeyJoinColumn
	@OneToOne(mappedBy= "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UserAccountActivationCodeEntity activationCode;

	@JsonCreator
	public UserAccountEntity(@JsonProperty(value="username", required=true) final String username, @JsonProperty(value="password", required=true) final String password) {
		this.password = password.trim();
		this.username = username.trim();
		this.activationCode = null;
		this.createTime = System.currentTimeMillis();
		this.resetCode = null;
	}

	public UserAccountEntity() {}

	public void setUser(final UserEntity user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.commaSeparatedStringToAuthorityList(ROLE_USER);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getId() {
		return id;
	}

	public String getActivationCode() {
		if (this.activationCode == null) {
			return null;
		}
		return activationCode.getActivationCode();
	}

	public boolean isActivationCodeExpired() {
		return this.activationCode != null
				&& System.currentTimeMillis() - this.createTime > EXPIRATION_TIME;		
	}

	public boolean isActive() {
		return this.activationCode == null;
	}

	public boolean isReset() {
		return this.resetCode != null;
	}

	public void activate() {
		this.activationCode = null;
	}

	public void setActivationCode(UserAccountActivationCodeEntity activityCode) {
		this.activationCode = activityCode;
	}

	public void setResetCode(UserAccountResetCodeEntity resetCode) {
		this.resetCode = resetCode;
	}

	public String getResetCode() {
		if (this.resetCode == null) {
			return null;
		}
		return resetCode.getResetCode();
	}

	public void setPassword(String encode) {
		
	}
}
