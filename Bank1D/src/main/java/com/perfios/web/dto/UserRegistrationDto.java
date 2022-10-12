package com.perfios.web.dto;

import java.sql.Date;

public class UserRegistrationDto {
	private String creditCard;
	public String getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private int balance;
	private long accountNumber;
	private int pin;
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	private int age;
	private String gender;
	private long contact;
	private String state;
	private String country;
	private String city;
	public UserRegistrationDto(){
		
	}
	public UserRegistrationDto(String firstName, 
			String lastName,
			String email, String password,
			int balance,
			int age, long accountNumber,
			String gender, 
			long contact,
			String state, 
			String country, 
			String city) {
		super();
		this.accountNumber=accountNumber;
		this.balance=balance;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.age = age;
		this.gender = gender;
		this.contact = contact;
		this.state = state;
		this.country = country;
		this.city = city;
	}


	
	
	



	public int getAge() {
		return age;
	}



	public void setAge(int age) {
		this.age = age;
	}



	public String getGender() {
		return gender;
	}



	public void setGender(String gender) {
		this.gender = gender;
	}



	public long getContact() {
		return contact;
	}



	public void setContact(long contact) {
		this.contact = contact;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}





	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}