package com.karen.moneylizer.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karen.moneylizer.core.entity.useraccount.UserAccountEntity;

public interface UserAccountRepository extends
		JpaRepository<UserAccountEntity, String> {

	boolean existsByUsername(String username);

	UserAccountEntity findByUsername(String username);

}