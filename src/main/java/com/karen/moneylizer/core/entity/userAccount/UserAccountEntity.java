package com.karen.moneylizer.core.entity.userAccount;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.karen.moneylizer.core.entity.user.UserEntity;
import com.karen.moneylizer.core.entity.userAccountResetCodeEntity.UserAccountResetCodeEntity;
import com.karen.moneylizer.core.entity.usernameConfirmationCode.UsernameConfirmationEntity;
import com.karen.moneylizer.core.utils.RandomAlphanumericIdGenerator;

@Entity
@Table(name="user_account_entity")
@JsonSerialize(using = UserAccountSerializer.class)
public class UserAccountEntity implements UserDetails {

	private static final long serialVersionUID = -1662173405386513224L;
	private final static String ROLE_USER  = "ROLE_USER";
	private final static long EXPIRATION_TIME = TimeUnit.DAYS.toMillis(10);
	private final static long FAILURE_COUNT_PERIOD = TimeUnit.HOURS.toMillis(1);
	private final static int FAILURE_COUNT_LIMIT = 5;
	
	@Getter
	@Id
	@GeneratedValue(generator = RandomAlphanumericIdGenerator.generatorName)
	@GenericGenerator(name = RandomAlphanumericIdGenerator.generatorName, strategy = "com.karen.moneylizer.core.utils.RandomAlphanumericIdGenerator")
	private String id;

	@Getter
	private String username;

	@Setter
	@Getter
	private String password;

	private Long lastFailedLoginTime;
	/**
	 * failedLoginCount is the number of failed logins within the last
	 * n seconds
	 */
	private int failedLoginCount;
	private long createTime;

	@Getter
	@Setter
	@PrimaryKeyJoinColumn
	@OneToOne(mappedBy= "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UserAccountResetCodeEntity resetCode; // TODO can we change this to optional

	@PrimaryKeyJoinColumn
	@OneToOne(mappedBy= "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UserEntity user;

	@PrimaryKeyJoinColumn
	@OneToOne(mappedBy= "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UsernameConfirmationEntity confirmationEntity; //TODO can we change it to Optional

	@JsonCreator
	public UserAccountEntity(@JsonProperty(value="username", required=true) final String username, @JsonProperty(value="password", required=true) final String password) {
		this.password = password.trim();
		this.username = username.trim();
		this.confirmationEntity = null;
		this.createTime = System.currentTimeMillis();
		this.resetCode = null;
		this.lastFailedLoginTime = null;
		this.failedLoginCount = 0;
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
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.failedLoginCount < FAILURE_COUNT_LIMIT || !loginFailedWithinLastHour(); 
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getConfirmationCode() {
		if (this.confirmationEntity == null) {
			return null;
		}
		return confirmationEntity.getConfirmationCode();
	}

	public boolean isConfirmationCodeExpired() {
		return this.confirmationEntity != null
				&& System.currentTimeMillis() - this.createTime > EXPIRATION_TIME;
	}

	public boolean isUsernameConfirmed() {
		return this.confirmationEntity == null;
	}

	/**
	 * Is there a reset token associated with the account
	 * i.e. did user request to reset the password and it didn't reset it yet?
	 * @return
	 */
	public boolean isReset() {
		return this.resetCode != null;
	}

	public void confirmUsername() {
		this.confirmationEntity = null;
	}

	public void setConfirmationCode(UsernameConfirmationEntity confirmationCode) {
		this.confirmationEntity = confirmationCode;
	}

	public String getResetCodeValue() {
		if (this.resetCode == null) {
			return null;
		}
		return resetCode.getResetCode();
	}

	public void increaseFailedLogin() {
		if (!loginFailedWithinLastHour()) {
			this.resetFailedLogin();
		}
		this.failedLoginCount++;
		this.failedLoginCount = Math.min(this.failedLoginCount , FAILURE_COUNT_LIMIT);
		this.lastFailedLoginTime = System.currentTimeMillis();
	}

	public void resetFailedLogin() {
		this.failedLoginCount = 0;
	}

	private boolean loginFailedWithinLastHour() {
		return System.currentTimeMillis() - this.lastFailedLoginTime < FAILURE_COUNT_PERIOD;
	}

	public void resetConfirmationCode() {
		this.confirmationEntity = new UsernameConfirmationEntity();
	}
}