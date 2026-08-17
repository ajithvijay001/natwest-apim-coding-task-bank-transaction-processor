package com.natwest.bank_transaction_processor.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.natwest.bank_transaction_processor.model.Accounts;
import com.natwest.bank_transaction_processor.model.enums.TransactionType;

@Service
public class AccountService {
	
	private final TransactionService transactionService;
	
	AccountService(TransactionService transactionService){
		this.transactionService = transactionService;
	}

	Map<Long, Accounts> accountMap = new HashMap<>();
	public Accounts createAccount(String accountName, BigDecimal amount) {
		
		if(amount.compareTo(BigDecimal.ZERO)<0) throw new IllegalArgumentException("Initial balance cannot be negative");
		Accounts newAccount = new Accounts(accountName, amount);
		newAccount.setAccountId(ThreadLocalRandom.current().nextLong(1000000000000000L, 10000000000000000L));
		newAccount.setCreatedAt(LocalDateTime.now());
		
		accountMap.put(newAccount.getAccountId(),newAccount);
		
		return newAccount; 
	}
	
	public Accounts deposit(Long accountId, BigDecimal depositAmount) {
		
		if(!accountMap.containsKey(accountId)) throw new IllegalArgumentException("Account Id does not exists");
		
		if(depositAmount.compareTo(BigDecimal.ZERO)<=0) throw new IllegalArgumentException("Deposit amount should be greater than 0");
		
		Accounts account = accountMap.get(accountId);
		account.setBalance(account.getBalance().add(depositAmount));
		transactionService.recordTransaction(accountId, TransactionType.CREDIT, depositAmount, account.getBalance());
		
		return account;
	}
	
	public Accounts withdraw(Long accountId, BigDecimal withdrawAmount) {
		
		if(!accountMap.containsKey(accountId)) throw new IllegalArgumentException("Account Id does not exists.");
		
		if(withdrawAmount.compareTo(BigDecimal.ZERO)<=0) throw new IllegalArgumentException("Withdrawal amount should be greater than 0.");
		
		Accounts account = accountMap.get(accountId);
		if(account.getBalance().compareTo(withdrawAmount)<0) throw new IllegalArgumentException("Insufficient Balance.");
		
		account.setBalance(account.getBalance().subtract(withdrawAmount));
		
		transactionService.recordTransaction(accountId, TransactionType.DEBIT, withdrawAmount, account.getBalance());
		
		return account;
	}
	
	public void transfer(Long fromAccount, Long toAccount, BigDecimal transferAmount) {
		
		if(fromAccount.equals(toAccount)) throw new IllegalArgumentException("Self transfer is restricted");
		
		if(!accountMap.containsKey(fromAccount)) throw new IllegalArgumentException("Source account does not exists");
		
		if(!accountMap.containsKey(toAccount)) throw new IllegalArgumentException("Desination account does not exists");
		
		if(transferAmount.compareTo(BigDecimal.ZERO)<=0) throw new IllegalArgumentException("Transfer amount should be greater than 0.");
		
		if(accountMap.get(fromAccount).getBalance().compareTo(transferAmount)<0) throw new IllegalArgumentException("Insufficent Balance in source account");
		
		withdraw(fromAccount, transferAmount);
		deposit(toAccount, transferAmount);
		
	}
}
