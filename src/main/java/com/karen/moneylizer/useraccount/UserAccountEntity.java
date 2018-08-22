package com.karen.moneylizer.useraccount;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.karen.moneylizer.user.UserEntity;

@Entity
@Table(name="user_account_entity")
@JsonDeserialize(using = UserAccountDeserializer.class)
public class UserAccountEntity implements UserDetails {

	private static final long serialVersionUID = -1662173405386513224L;
	private final static String ROLE_USER  = "ROLE_USER";

	@Id
	@NotBlank
	private String id;

	@NotBlank
	private final String username;

	@NotBlank
	private final String password;

	@NotNull
	@MapsId
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UserEntity user;

	public UserAccountEntity(final String username, final String password) {
		this.password = password;
		this.username = username;
	}

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
