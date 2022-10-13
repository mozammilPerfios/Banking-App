package com.perfios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.perfios.model.CreditCardTable;
import com.perfios.model.RepayTable;

public interface RepayRepository extends JpaRepository<RepayTable, Long>{
	@Query
	(value="SELECT * FROM repay_table where email=?1 and is_repayed='No'",nativeQuery=true)
	List<RepayTable> findByRepaid(String name);

	List<RepayTable> findByEmail(String name);

}