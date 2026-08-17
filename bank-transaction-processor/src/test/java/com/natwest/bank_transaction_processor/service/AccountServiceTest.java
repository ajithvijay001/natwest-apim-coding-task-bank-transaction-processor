package com.natwest.bank_transaction_processor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.natwest.bank_transaction_processor.model.Accounts;

public class AccountServiceTest {
	
	private final AccountService accountService = new AccountService();

	@Test
	void createAccount_withValidAmount_setBalanceCorrectly() {
		
		String accountName = "John";
		BigDecimal amount = new BigDecimal("10000.00");
		
		Accounts account = accountService.createAccount(accountName, amount);
		
		assertEquals(amount, account.getBalance());
		assertNotNull(account.getAccountId());
	}
	
	@Test
	void createAccount_withNegativeAmount_throwsException() {
		String accountName = "Mafi";
		BigDecimal amount = new BigDecimal("-10000.00");
		
		assertThrows( IllegalArgumentException.class, () -> {accountService.createAccount(accountName, amount);});
		
	}
	
	@Test
	void deposit_withValidAmount_toValidAccountCorrectly() {
		String accountName = "John";
		BigDecimal amount = new BigDecimal("10000.00");
		BigDecimal depositAmount = new BigDecimal("5000.00");
		Accounts account = accountService.createAccount(accountName, amount);
		accountService.deposit(account.getAccountId(),depositAmount);
		
		assertEquals(new BigDecimal("15000.00"), account.getBalance());
	}
	
	@Test
	void deposit_toNonExistigAccount_throwsException() {
		BigDecimal depositAmount = new BigDecimal("5000.00");
		
		assertThrows(IllegalArgumentException.class,() -> {
			accountService.deposit(0L, depositAmount);
		});
	}
	
	@Test
	void deposit_withNegativeAmount_throwsException() {
		String accountName = "John";
		BigDecimal amount = new BigDecimal("10000.00");
		BigDecimal depositAmount = new BigDecimal("-5000.00");
		Accounts account = accountService.createAccount(accountName, amount);
		
		assertThrows(IllegalArgumentException.class,() -> {accountService.deposit(account.getAccountId(),depositAmount);});
	}
	
	@Test
	void withdraw_withValidAmount_fromValidAccountCorrectly() {
		String accountName ="Mafi";
		BigDecimal amount = new BigDecimal("10000.00");
		BigDecimal withdrawAmount = new BigDecimal("5000.00");
		Accounts account = accountService.createAccount(accountName, amount);
		accountService.withdraw(account.getAccountId(),withdrawAmount);
		
		assertEquals(new BigDecimal("5000.00"), account.getBalance());
		
	}
	
	@Test
	void withdraw_fromNonExistingAccount_throwsException() {
		BigDecimal withdrawAmount = new BigDecimal("5000.00");
		
		assertThrows(IllegalArgumentException.class,() -> {
			accountService.deposit(0L, withdrawAmount);
		});
	}
	
	@Test
	void withdraw_withNegativeAmount_throwsException() {
		String accountName = "John";
		BigDecimal amount = new BigDecimal("10000.00");
		BigDecimal withdrawAmount = new BigDecimal("-5000.00");
		Accounts account = accountService.createAccount(accountName, amount);
		
		assertThrows(IllegalArgumentException.class,() -> {accountService.withdraw(account.getAccountId(),withdrawAmount);});
	}
	
	@Test
	void withdraw_withValidAmount_insufficientBalance_throwsException() {
		String accountName = "John";
		BigDecimal amount = new BigDecimal("10000.00");
		BigDecimal withdrawAmount = new BigDecimal("15000.00");
		Accounts account = accountService.createAccount(accountName, amount);
		
		assertThrows(IllegalArgumentException.class,() -> {accountService.withdraw(account.getAccountId(),withdrawAmount);});
	}

}
