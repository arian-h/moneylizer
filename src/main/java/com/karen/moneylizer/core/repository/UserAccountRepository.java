package com.karen.moneylizer.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;

public interface UserAccountRepository extends
		JpaRepository<UserAccountEntity, String> {

	UserAccountEntity findByUsername(String username);

}