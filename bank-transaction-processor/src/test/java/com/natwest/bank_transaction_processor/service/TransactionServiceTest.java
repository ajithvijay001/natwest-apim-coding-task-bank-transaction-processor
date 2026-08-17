package com.natwest.bank_transaction_processor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.natwest.bank_transaction_processor.model.Transaction;
import com.natwest.bank_transaction_processor.model.enums.TransactionType;

public class TransactionServiceTest {
	
	private final TransactionService transactionService = new TransactionService();

	@Test
	void recordTransaction_withValidDetails_storesTransactionForAccount() {
		Long accountId = 1234567891234567L;
		BigDecimal amount= new BigDecimal("5000.00");
		BigDecimal remainingBalance = new BigDecimal("15000.00");
		
		transactionService.recordTransaction(accountId,TransactionType.CREDIT, amount, remainingBalance);
		
		List<Transaction> history = transactionService.getTransactionHistory(accountId);
		
		assertEquals(1, history.size());
		Transaction recorded = history.get(0);
		assertEquals(accountId, recorded.accountId());
		assertEquals(TransactionType.CREDIT, recorded.transactionType());
		assertEquals(amount, recorded.amount());
        assertEquals(remainingBalance, recorded.balance());
        assertNotNull(recorded.transactionId());
        assertNotNull(recorded.transactionTimestamp());
	}
	
	@Test
	void recordTransaction_multipleTransactionForAccount_storesTransactionsForAccount() {
		Long accountId = 1234567891234567L;
		BigDecimal amount= new BigDecimal("5000.00");
		BigDecimal remainingBalance = new BigDecimal("15000.00");
		
		transactionService.recordTransaction(accountId,TransactionType.CREDIT, amount, remainingBalance);
		
		BigDecimal debitAmount = new BigDecimal("4000");
		BigDecimal debitRemainingBalance = new BigDecimal("11000.00");
		transactionService.recordTransaction(accountId, TransactionType.DEBIT,debitAmount, debitRemainingBalance);
		
		List<Transaction> history = transactionService.getTransactionHistory(accountId);
		
		assertEquals(2, history.size());
		Transaction recorded = history.get(0);
		assertEquals(accountId, recorded.accountId());
		assertEquals(TransactionType.CREDIT, recorded.transactionType());
		assertEquals(amount, recorded.amount());
        assertEquals(remainingBalance, recorded.balance());
        assertNotNull(recorded.transactionId());
        assertNotNull(recorded.transactionTimestamp());
        
        recorded = history.get(1);
		assertEquals(accountId, recorded.accountId());
		assertEquals(TransactionType.DEBIT, recorded.transactionType());
		assertEquals(debitAmount, recorded.amount());
        assertEquals(debitRemainingBalance, recorded.balance());
        assertNotNull(recorded.transactionId());
        assertNotNull(recorded.transactionTimestamp());
	}
	
	@Test
	void recordTransaction_forDifferentAccounts_keepsHistoriesSeparate() {
	    Long accountA = 1111111111111111L;
	    Long accountB = 2222222222222222L;

	    transactionService.recordTransaction(accountA, TransactionType.CREDIT,
	            new BigDecimal("5000.00"), new BigDecimal("15000.00"));

	    transactionService.recordTransaction(accountB, TransactionType.CREDIT,
	            new BigDecimal("2000.00"), new BigDecimal("7000.00"));

	    List<Transaction> historyA = transactionService.getTransactionHistory(accountA);
	    List<Transaction> historyB = transactionService.getTransactionHistory(accountB);

	    assertEquals(1, historyA.size());
	    assertEquals(1, historyB.size());
	    assertEquals(accountA, historyA.get(0).accountId());
	    assertEquals(accountB, historyB.get(0).accountId());
	}
	
	@Test
	void getTransactionHistory_forAccountWithNoTransactions_returnsEmptyList() {
	    Long accountId = 9999999999999999L;

	    List<Transaction> history = transactionService.getTransactionHistory(accountId);

	    assertNotNull(history);
	    assertEquals(0, history.size());
	}
	
	
	
}
