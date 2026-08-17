package com.natwest.bank_transaction_processor.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.natwest.bank_transaction_processor.model.Accounts;

public class AccountService {

	Map<Long, Accounts> accountMap = new HashMap<>();
	public Accounts createAccount(String accountName, BigDecimal amount) {
		
		if(amount.compareTo(BigDecimal.ZERO)<0) throw new IllegalArgumentException("Initial balance cannot be negative");
		Accounts newAccount = new Accounts(accountName, amount);
		newAccount.setAccountId(ThreadLocalRandom.current().nextLong(1000000000000000L, 10000000000000000L));
		newAccount.setCreatedAt(LocalDateTime.now());
		
		accountMap.put(newAccount.getAccountId(),newAccount);
		
		return newAccount; 
	}
	
	public BigDecimal deposit(Long accountId, BigDecimal depositAmount) {
		
		if(!accountMap.containsKey(accountId)) throw new IllegalArgumentException("Account Id does not exists");
		
		if(depositAmount.compareTo(BigDecimal.ZERO)<=0) throw new IllegalArgumentException("Deposit amount should be greater than 0");
		
		Accounts account = accountMap.get(accountId);
		account.setBalance(account.getBalance().add(depositAmount));
		accountMap.put(accountId, account);
		
		return account.getBalance();
	}
}
