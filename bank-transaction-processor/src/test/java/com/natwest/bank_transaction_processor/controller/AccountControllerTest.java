package com.natwest.bank_transaction_processor.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.natwest.bank_transaction_processor.model.Accounts;
import com.natwest.bank_transaction_processor.model.Transaction;
import com.natwest.bank_transaction_processor.model.enums.TransactionType;
import com.natwest.bank_transaction_processor.service.AccountService;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @Test
    void createAccount_withValidRequest_returns200AndAccountDetails() throws Exception {
        Accounts mockAccount = new Accounts("John", new BigDecimal("10000.00"));
        mockAccount.setAccountId(1234567890123456L);

        when(accountService.createAccount("John", new BigDecimal("10000.00")))
                .thenReturn(mockAccount);

        String requestBody = """
                {
                  "accountName": "John",
                  "initialBalance": 10000.00
                }
                """;

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1234567890123456L))
                .andExpect(jsonPath("$.accountName").value("John"));
    }
    
    @Test
    void deposit_withValidRequest_returns200AndUpdatedAccount() throws Exception {
        Accounts mockAccount = new Accounts("John", new BigDecimal("15000.00"));
        mockAccount.setAccountId(1234567890123456L);

        when(accountService.deposit(1234567890123456L, new BigDecimal("5000.00")))
                .thenReturn(mockAccount);

        String requestBody = """
                { "amount": 5000.00 }
                """;

        mockMvc.perform(post("/accounts/1234567890123456/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(15000.00));
    }
    
    @Test
    void withdraw_withValidRequest_returns200AndUpdatedAccount() throws Exception {
        Accounts mockAccount = new Accounts("John", new BigDecimal("7000.00"));
        mockAccount.setAccountId(1234567890123456L);

        when(accountService.withdraw(1234567890123456L, new BigDecimal("3000.00")))
                .thenReturn(mockAccount);

        String requestBody = """
                { "amount": 3000.00 }
                """;

        mockMvc.perform(post("/accounts/1234567890123456/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(7000.00));
    }
    
    @Test
    void transfer_withValidRequest_returns200() throws Exception {
        Accounts mockFromAccount = new Accounts("Mafi", new BigDecimal("7000.00"));
        mockFromAccount.setAccountId(1111111111111111L);

        doNothing().when(accountService)
                .transfer(1111111111111111L, 2222222222222222L, new BigDecimal("3000.00"));

        String requestBody = """
                {
                  "fromAccountId": 1111111111111111,
                  "toAccountId": 2222222222222222,
                  "amount": 3000.00
                }
                """;

        mockMvc.perform(post("/accounts/transfer")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody))
        .andExpect(status().isOk());
    }
    
    @Test
    void getBalance_forExistingAccount_returns200AndBalance() throws Exception {
        Accounts mockAccount = new Accounts("John", new BigDecimal("10000.00"));
        mockAccount.setAccountId(1234567890123456L);

        when(accountService.getAccount(1234567890123456L)).thenReturn(mockAccount);

        mockMvc.perform(get("/accounts/1234567890123456/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1234567890123456L))
                .andExpect(jsonPath("$.balance").value(10000.00));
    }
    
    @Test
    void getTransactionHistory_forExistingAccount_returns200AndHistory() throws Exception {
        Transaction mockTransaction = new Transaction(
                9999999999999999L, 1234567890123456L, TransactionType.CREDIT,
                new BigDecimal("5000.00"), new BigDecimal("15000.00"), LocalDateTime.now());

        when(accountService.getTransactionHistory(1234567890123456L))
                .thenReturn(List.of(mockTransaction));

        mockMvc.perform(get("/accounts/1234567890123456/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value(1234567890123456L))
                .andExpect(jsonPath("$[0].amount").value(5000.00));
    }
    
}
