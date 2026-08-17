package com.natwest.bank_transaction_processor.dto;

import java.math.BigDecimal;

public class CreateAccountRequest {
	
	private String accountName;
    private BigDecimal initialBalance;

    public String getAccountName() { 
    	return accountName; 
    }
    
    public void setAccountName(String accountName) { 
    	this.accountName = accountName; 
    }

    public BigDecimal getInitialBalance() { 
    	return initialBalance; 
    }
    
    public void setInitialBalance(BigDecimal initialBalance) {
    	this.initialBalance = initialBalance; 
    }

}
