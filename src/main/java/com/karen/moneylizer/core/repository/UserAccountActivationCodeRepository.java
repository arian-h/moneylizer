package com.karen.moneylizer.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karen.moneylizer.core.entity.userAccountActivationCode.UserAccountActivationCodeEntity;

public interface UserAccountActivationCodeRepository extends
		JpaRepository<UserAccountActivationCodeEntity, String> {}