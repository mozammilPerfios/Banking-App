package com.perfios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.perfios.model.TransactionTable;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionTable, Long>{
	@Query
	(value="SELECT * FROM transaction where credit_account=?1 or debit_account=?1",nativeQuery=true)
	List<TransactionTable> findByAccount(long accountNumber);
	
}