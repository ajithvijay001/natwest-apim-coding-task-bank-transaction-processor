package com.natwest.bank_transaction_processor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.natwest.bank_transaction_processor.model.enums.TransactionType;

public record Transaction(
		Long transactionId,
		Long accountId, 
		TransactionType transactionType,
		BigDecimal amount,
		BigDecimal balance,
		LocalDateTime transactionTimestamp) {}
