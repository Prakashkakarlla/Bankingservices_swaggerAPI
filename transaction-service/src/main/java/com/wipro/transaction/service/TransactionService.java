package com.wipro.transaction.service;

import com.wipro.transaction.entity.Transaction;
import com.wipro.transaction.event.NotificationEvent;
import com.wipro.transaction.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public Transaction deposit(String accountId, Double amount) {
        if (accountId == null || amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid account ID or amount");
        }
        // Call Account Service to adjust balance
        String url = "http://account-service/api/accounts/" + accountId + "/adjust";
        restTemplate.postForObject(url, amount, Void.class);
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        eventPublisher.publishEvent(new NotificationEvent(this, "TRANSACTION_DEPOSIT", accountId, "Deposited: " + amount, "user@example.com"));
        return savedTransaction;
    }

    @Transactional
    public Transaction withdraw(String accountId, Double amount) {
        if (accountId == null || amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid account ID or amount");
        }
        // Call Account Service to adjust balance
        String url = "http://account-service/api/accounts/" + accountId + "/adjust";
        restTemplate.postForObject(url, -amount, Void.class);
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setType("WITHDRAWAL");
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        eventPublisher.publishEvent(new NotificationEvent(this, "TRANSACTION_WITHDRAWAL", accountId, "Withdrawn: " + amount, "user@example.com"));
        return savedTransaction;
    }

    public List<Transaction> getTransactions(String accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID is required");
        }
        return transactionRepository.findByAccountId(accountId);
    }
}

