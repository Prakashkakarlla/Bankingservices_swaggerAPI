package com.wipro.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.transaction.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(String accountId);
}