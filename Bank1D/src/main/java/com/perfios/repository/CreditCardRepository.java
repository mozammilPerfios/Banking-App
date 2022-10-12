package com.perfios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perfios.model.CreditCardTable;

public interface CreditCardRepository extends JpaRepository<CreditCardTable, Long>{

	CreditCardTable findByEmail(String name);
}