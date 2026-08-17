package com.natwest.bank_transaction_processor.dto;

import java.math.BigDecimal;

public class AmountRequest {
	
	private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
