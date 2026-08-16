package com.natwest.bank_transaction_processor.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import com.natwest.bank_transaction_processor.model.Accounts;

public class AccountService {

	public Accounts createAccount(String accountName, BigDecimal amount) {
		
		if(amount.compareTo(BigDecimal.ZERO)<0) throw new IllegalArgumentException("Initial balance cannot be negative");
		Accounts newAccount = new Accounts(accountName, amount);
		newAccount.setAccountId(ThreadLocalRandom.current().nextLong(1000000000000000L, 10000000000000000L));
		newAccount.setCreatedAt(LocalDateTime.now());
		
		return newAccount; 
	}
}
