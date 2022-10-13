package com.perfios.web.dto;

import java.util.Date;

public class CreditCardTableDto {
	private Long cardNumber;
	private int creditUsed;
	private Date repayableDate;
	private int timesCardUsed;
	private int creditBalance;
	private String pan;
	private int adhaar;
	private String name;
	private int cvv;
	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	private String accountNumber;
	public Long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getCreditUsed() {
		return creditUsed;
	}
	public void setCreditUsed(int creditUsed) {
		this.creditUsed = creditUsed;
	}
	public Date getRepayableDate() {
		return repayableDate;
	}
	public void setRepayableDate(Date repayableDate) {
		this.repayableDate = repayableDate;
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
}
