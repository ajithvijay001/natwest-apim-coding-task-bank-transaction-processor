package com.natwest.bank_transaction_processor.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.natwest.bank_transaction_processor.model.Transaction;
import com.natwest.bank_transaction_processor.model.enums.TransactionType;

public class TransactionService {
	
	private final Map<Long, List<Transaction>> transactionMap = new HashMap<>();

    public void recordTransaction(Long accountId, TransactionType type, BigDecimal amount, BigDecimal resultingBalance) {

        Long transactionId = ThreadLocalRandom.current().nextLong(1000000000000000L, 10000000000000000L);
        Transaction transaction = new Transaction(transactionId, accountId, type, amount, resultingBalance, LocalDateTime.now());

        transactionMap.computeIfAbsent(accountId, id -> new ArrayList<>()).add(transaction);
    }

    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionMap.getOrDefault(accountId, new ArrayList<>());
    }
    
}
