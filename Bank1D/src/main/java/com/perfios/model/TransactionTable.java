package com.perfios.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name =  "transaction")
public class TransactionTable {
	private int amount;
	private String transTime;
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	private String creditAccount;
	private String debitAccount;
	private String creditOrDebit;
	private int balance;
	private String transactionType;
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="UserId",referencedColumnName="id")
	private User user;
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long transactionId;
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getCreditAccount() {
		return creditAccount;
	}
	public void setCreditAccount(String creditAccount) {
		this.creditAccount = creditAccount;
	}
	public String getDebitAccount() {
		return debitAccount;
	}
	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}
	public String getCreditOrDebit() {
		return creditOrDebit;
	}
	public void setCreditOrDebit(String creditOrDebit) {
		this.creditOrDebit = creditOrDebit;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	

}
