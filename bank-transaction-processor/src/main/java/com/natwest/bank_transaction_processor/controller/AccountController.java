package com.natwest.bank_transaction_processor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natwest.bank_transaction_processor.dto.AmountRequest;
import com.natwest.bank_transaction_processor.dto.CreateAccountRequest;
import com.natwest.bank_transaction_processor.dto.TransferReqeust;
import com.natwest.bank_transaction_processor.model.Accounts;
import com.natwest.bank_transaction_processor.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Accounts> createAccount(@RequestBody CreateAccountRequest request) {
        Accounts account = accountService.createAccount(request.getAccountName(), request.getInitialBalance());
        return ResponseEntity.ok(account);
    }
    
    @PostMapping("/{id}/deposit")
    public ResponseEntity<Accounts> deposit(@PathVariable Long id, @RequestBody AmountRequest request) {
        Accounts account = accountService.deposit(id, request.getAmount());
        return ResponseEntity.ok(account);
    }
    
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Accounts> withdraw(@PathVariable Long id, @RequestBody AmountRequest request) {
        Accounts account = accountService.withdraw(id, request.getAmount());
        return ResponseEntity.ok(account);
    }
    

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferReqeust request) {
        accountService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}
