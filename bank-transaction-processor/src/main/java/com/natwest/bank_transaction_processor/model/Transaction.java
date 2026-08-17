package com.natwest.bank_transaction_processor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.natwest.bank_transaction_processor.model.enums.TransactionType;

public class Transaction {
	
	private Long transactionId;
	
	private Long accountId;
	
	private TransactionType transactionType;
	
	private BigDecimal amount;
	
	private BigDecimal balance;
	
	private LocalDateTime transactionTimestamp;
	
	public Transaction(Long transactionId, Long accountId, TransactionType transactionType, BigDecimal amount, 
			BigDecimal balance, LocalDateTime transactionTimestamp) {
		this.transactionId = transactionId;
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.balance = balance;
		this.transactionTimestamp = transactionTimestamp;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public LocalDateTime getTransactionTimestamp() {
		return transactionTimestamp;
	}
	
}
