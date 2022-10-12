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
@Table(name =  "creditCardTable", uniqueConstraints = @UniqueConstraint(columnNames = "cardNumber"))
public class CreditCardTable {
	
	public Long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public int getAdhaar() {
		return adhaar;
	}
	public void setAdhaar(int adhaar) {
		this.adhaar = adhaar;
	}
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	private int repayableAmount;
	public int getRepayableAmount() {
		return repayableAmount;
	}
	public void setRepayableAmount(int repayableAmount) {
		this.repayableAmount = repayableAmount;
	}
	private Long cardNumber;
	private int creditUsed;
	private LocalDate repayableDate;
	private int timesCardUsed;
	private int creditBalance;
	private String pan;
	private int adhaar;
	private String name;
	private int cvv;
	private String email;
	private int warningIssued;
	public int getWarningIssued() {
		return warningIssued;
	}
	public void setWarningIssued(int warningIssued) {
		this.warningIssued = warningIssued;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	private Long accountNumber;
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
	public int getTimesCardUsed() {
		return timesCardUsed;
	}
	public void setTimesCardUsed(int timesCardUsed) {
		this.timesCardUsed = timesCardUsed;
	}
	public int getCreditBalance() {
		return creditBalance;
	}
	public void setCreditBalance(int creditBalance) {
		this.creditBalance = creditBalance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCvv() {
		return cvv;
	}
	public void setCvv(int cvv) {
		this.cvv = cvv;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	private long creditCardNumber;
	private int amountRepayed;
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="UserId",referencedColumnName="id")
	private User user;
}
	