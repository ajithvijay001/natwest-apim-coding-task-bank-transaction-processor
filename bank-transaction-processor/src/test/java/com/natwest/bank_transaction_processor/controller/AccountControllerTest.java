package com.natwest.bank_transaction_processor.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.natwest.bank_transaction_processor.model.Accounts;
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
}
