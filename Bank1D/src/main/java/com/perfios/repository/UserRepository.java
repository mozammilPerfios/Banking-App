package com.perfios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfios.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);

	User findByaccountNumber(long accountNumber);
	
}