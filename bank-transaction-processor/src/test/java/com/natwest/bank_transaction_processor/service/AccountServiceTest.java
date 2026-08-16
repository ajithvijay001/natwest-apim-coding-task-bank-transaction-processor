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
}
