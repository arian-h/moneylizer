package com.karen.moneylizer.useraccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends
		JpaRepository<UserAccountEntity, String> {

	boolean existsByUsername(String username);

	UserAccountEntity findByUsername(String username);

}