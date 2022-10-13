package com.perfios.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name =  "repayTable")
public class RepayTable {
	

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	private Long accountNumber;
	private int repayableAmount;
	private Long cardNumber;
	private int creditUsed;
	private LocalDate repayableDate;
	private LocalDate transactionDate;
	public LocalDate getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}
	private String name;
	private String email;
	private int warningIssued;
	private long creditCardNumber;
	private int amountRepayed;
	private String isRepayed;
	public String getIsRepayed() {
		return isRepayed;
	}
	public void setIsRepayed(String isRepayed) {
		this.isRepayed = isRepayed;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="creditCardId",referencedColumnName="id")
	private CreditCardTable creditCardTable;
	public int getWarningIssued() {
		return warningIssued;
	}
	public void setWarningIssued(int warningIssued) {
		this.warningIssued = warningIssued;
	}
	public String getEmail() {
		return email;
	}
	public int getRepayableAmount() {
		return repayableAmount;
	}
	public void setRepayableAmount(int repayableAmount) {
		this.repayableAmount = repayableAmount;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getAccountNumber() {
		return accountNumber;
	}
	public Long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public void setAccountNumber(long l) {
		this.accountNumber = l;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getCreditUsed() {
		return creditUsed;
	}
	public void setCreditUsed(int creditUsed) {
		this.creditUsed = creditUsed;
	}
	public LocalDate getRepayableDate() {
		return repayableDate;
	}
	public void setRepayableDate(LocalDate localDate) {
		this.repayableDate = localDate;
	}
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public long getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(long creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public int getAmountRepayed() {
		return amountRepayed;
	}
	public void setAmountRepayed(int amountRepayed) {
		this.amountRepayed = amountRepayed;
	}
	public CreditCardTable getCreditCardTable() {
		return creditCardTable;
	}
	public void setCreditCardTable(CreditCardTable creditCardTable) {
		this.creditCardTable = creditCardTable;
	}
	
	
}
	