package com.karen.moneylizer.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karen.moneylizer.core.entity.userAccountResetCodeEntity.UserAccountResetCodeEntity;

public interface UserAccountResetCodeRepository extends
		JpaRepository<UserAccountResetCodeEntity, String> {
}
