package com.karen.moneylizer.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karen.moneylizer.core.entity.usernameConfirmationCode.UsernameConfirmationEntity;

public interface UsernameConfirmationRepository extends
		JpaRepository<UsernameConfirmationEntity, String> {}