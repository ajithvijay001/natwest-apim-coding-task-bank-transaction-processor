package com.natwest.bank_transaction_processor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Accounts {
	
	private Long accountId;
	
	private String accountName;
	
	private BigDecimal balance;
	
	private LocalDateTime createdAt;
	
	public Accounts(String accountName, BigDecimal balance){
		this.accountName = accountName;
		this.balance = balance;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
