package com.karen.moneylizer.investment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karen.moneylizer.investment.model.InvestmentEntity;

public interface InvestmentRepository extends JpaRepository<InvestmentEntity, Long> {
}
