package com.wipro.account.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.account.entity.Account;
import com.wipro.account.event.NotificationEvent;
import com.wipro.account.exception.ResourceNotFoundException;
import com.wipro.account.repository.AccountRepository;

import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public Account createAccount(Account account) {
        if (account.getType() == null || account.getType().isEmpty()) {
            throw new IllegalArgumentException("Account type is required");
        }
        account.setAccountNumber(UUID.randomUUID().toString());
        account.setStatus("ACTIVE");
        if (account.getBalance() == null) {
            account.setBalance(0.0);
        }
        Account savedAccount = accountRepository.save(account);
        eventPublisher.publishEvent(new NotificationEvent(this, "ACCOUNT_CREATED", savedAccount.getId().toString(), "New account created with type: " + account.getType(), "user@example.com"));
        return savedAccount;
    }

    @Transactional
    public Account updateAccount(Long id, Account accountDetails) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        if (account.getStatus().equals("CLOSED")) {
            throw new IllegalArgumentException("Cannot update closed account");
        }
        if (accountDetails.getType() == null || accountDetails.getType().isEmpty()) {
            throw new IllegalArgumentException("Account type is required");
        }
        account.setType(accountDetails.getType());
        Account updatedAccount = accountRepository.save(account);
        eventPublisher.publishEvent(new NotificationEvent(this, "ACCOUNT_UPDATED", account.getId().toString(), "Account type updated to: " + account.getType(), "user@example.com"));
        return updatedAccount;
    }

    @Transactional
    public Account adjustBalance(Long id, Double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        if (account.getStatus().equals("CLOSED")) {
            throw new IllegalArgumentException("Cannot adjust balance of closed account");
        }
        if (amount == null || amount == 0) {
            throw new IllegalArgumentException("Amount must be non-zero");
        }
        double newBalance = account.getBalance() + amount;
        if (newBalance < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        account.setBalance(newBalance);
        Account updatedAccount = accountRepository.save(account);
        eventPublisher.publishEvent(new NotificationEvent(this, "BALANCE_ADJUSTED", account.getId().toString(), "Balance adjusted by: " + amount + ", New balance: " + newBalance, "user@example.com"));
        return updatedAccount;
    }

    public Double getBalance(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return account.getBalance();
    }

    @Transactional
    public Account closeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        if (account.getStatus().equals("CLOSED")) {
            throw new IllegalArgumentException("Account is already closed");
        }
        account.setStatus("CLOSED");
        Account closedAccount = accountRepository.save(account);
        eventPublisher.publishEvent(new NotificationEvent(this, "ACCOUNT_CLOSED", account.getId().toString(), "Account closed", "user@example.com"));
        return closedAccount;
    }
}