package com.karen.moneylizer.core.entity.useraccount;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.karen.moneylizer.core.entity.user.UserEntity;

@Entity
@Table(name="user_account_entity")
@JsonDeserialize(using = UserAccountDeserializer.class)
@JsonSerialize(using = UserAccountSerializer.class)
public class UserAccountEntity implements UserDetails {

	private static final long serialVersionUID = -1662173405386513224L;
	private final static String ROLE_USER  = "ROLE_USER";

	@Id
	private String id;

	private String username;

	private String password;

	@MapsId
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UserEntity user;

	public UserAccountEntity(final String username, final String password) {
		this.password = password.trim();
		this.username = username.trim();
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

}
