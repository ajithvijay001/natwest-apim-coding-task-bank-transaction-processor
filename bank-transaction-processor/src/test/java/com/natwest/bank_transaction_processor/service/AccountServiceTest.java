package com.natwest.bank_transaction_processor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.natwest.bank_transaction_processor.model.Accounts;
import com.natwest.bank_transaction_processor.model.Transaction;
import com.natwest.bank_transaction_processor.model.enums.TransactionType;

public class AccountServiceTest {
	
	private final TransactionService transactionService = new TransactionService();
	private final AccountService accountService = new AccountService(transactionService);

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
			accountService.withdraw(0L, withdrawAmount);
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
	
	@Test
	void transfer_withValidAmount_betweenValidAccountsCorrectly() {
		
		Accounts fromAccount = accountService.createAccount("Mafi",new BigDecimal("10000.00"));
		Accounts toAccount = accountService.createAccount("John",new BigDecimal("2000.00"));
		BigDecimal transferAmount = new BigDecimal("3000.00");
		
		accountService.transfer(fromAccount.getAccountId(), toAccount.getAccountId(), transferAmount);
		
		assertEquals(new BigDecimal("7000.00"), fromAccount.getBalance());
		assertEquals(new BigDecimal("5000.00"), toAccount.getBalance());
	}
	
	@Test
	void transfer_toSameAccount_thorwsException() {
		Accounts account = accountService.createAccount("Mafi",new BigDecimal("10000.00"));
		BigDecimal transferAmount = new BigDecimal("2000.00");
		
		assertThrows(IllegalArgumentException.class, () ->{
			accountService.transfer(account.getAccountId(), account.getAccountId(), transferAmount);
		});
	}
	
	@Test
	void transfer_fromNonExistingAccount_throwsException() {
		
		Accounts toAccount = accountService.createAccount("John",new BigDecimal("2000.00"));
		BigDecimal transferAmount = new BigDecimal("2500.00");
		
		
		assertThrows(IllegalArgumentException.class,() -> {
			accountService.transfer(0L,toAccount.getAccountId(), transferAmount);
		});
	}
	
	@Test
	void transfer_toNonExistingAccount_throwsException() {
		
		Accounts fromAccount = accountService.createAccount("Mafi",new BigDecimal("10000.00"));		
		BigDecimal transferAmount = new BigDecimal("2500.00");
		
		assertThrows(IllegalArgumentException.class,() -> {
			accountService.transfer(fromAccount.getAccountId(), 0L, transferAmount);
		});
	}
	
	@Test
	void transfer_withValidAmount_insufficientBalance_throwsException() {
		Accounts fromAccount = accountService.createAccount("Mafi",new BigDecimal("1500.00"));
		Accounts toAccount = accountService.createAccount("John",new BigDecimal("2000.00"));
		BigDecimal transferAmount = new BigDecimal("3000.00");
		
		assertThrows(IllegalArgumentException.class,() -> {
			accountService.transfer(fromAccount.getAccountId(), toAccount.getAccountId(),transferAmount);
			});
	}
	
	@Test
	void transfer_withInvalidAmount_betweenValidAccount_throwsException() {
		Accounts fromAccount = accountService.createAccount("Mafi",new BigDecimal("10000.00"));
		Accounts toAccount = accountService.createAccount("John",new BigDecimal("2000.00"));
		BigDecimal transferAmount = new BigDecimal("-2000.00");
		
		assertThrows(IllegalArgumentException.class, () ->{
			accountService.transfer(fromAccount.getAccountId(), toAccount.getAccountId(), transferAmount);
		} );
	}
	
	@Test
	void deposit_withValidAmount_recordsTransactionInLedger() {
	    Accounts account = accountService.createAccount("John", new BigDecimal("10000.00"));
	    BigDecimal depositAmount = new BigDecimal("5000.00");

	    accountService.deposit(account.getAccountId(), depositAmount);

	    List<Transaction> history = transactionService.getTransactionHistory(account.getAccountId());

	    assertEquals(1, history.size());
	    assertEquals(TransactionType.CREDIT, history.get(0).transactionType());
	    assertEquals(depositAmount, history.get(0).amount());
	}
	
	@Test
	void withdraw_withValidAmount_recordsTransactionInLedger() {
	    Accounts account = accountService.createAccount("John", new BigDecimal("10000.00"));
	    BigDecimal withdrawAmount = new BigDecimal("3000.00");

	    accountService.withdraw(account.getAccountId(), withdrawAmount);

	    List<Transaction> history = transactionService.getTransactionHistory(account.getAccountId());

	    assertEquals(1, history.size());
	    assertEquals(TransactionType.DEBIT, history.get(0).transactionType());
	    assertEquals(withdrawAmount, history.get(0).amount());
	}
	
	@Test
	void transfer_withValidAmount_recordsTransactionsForBothAccounts() {
	    Accounts fromAccount = accountService.createAccount("Mafi", new BigDecimal("10000.00"));
	    Accounts toAccount = accountService.createAccount("John", new BigDecimal("2000.00"));
	    BigDecimal transferAmount = new BigDecimal("3000.00");

	    accountService.transfer(fromAccount.getAccountId(), toAccount.getAccountId(), transferAmount);

	    List<Transaction> fromHistory = transactionService.getTransactionHistory(fromAccount.getAccountId());
	    List<Transaction> toHistory = transactionService.getTransactionHistory(toAccount.getAccountId());

	    assertEquals(1, fromHistory.size());
	    assertEquals(TransactionType.DEBIT, fromHistory.get(0).transactionType());

	    assertEquals(1, toHistory.size());
	    assertEquals(TransactionType.CREDIT, toHistory.get(0).transactionType());
	}

}
