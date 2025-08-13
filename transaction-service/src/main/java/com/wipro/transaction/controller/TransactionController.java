package com.wipro.transaction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wipro.transaction.entity.Transaction;
import com.wipro.transaction.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Deposit money into an account",
               description = "Adds the specified amount to the given account's balance.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deposit successful"),
        @ApiResponse(responseCode = "400", description = "Invalid account ID or amount")
    })
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(
            @Parameter(description = "Account ID where money will be deposited")
            @RequestParam @NotBlank(message = "Account ID cannot be blank") String accountId,

            @Parameter(description = "Amount to deposit (must be positive)")
            @RequestParam @NotNull(message = "Amount cannot be null") @Positive(message = "Amount must be greater than 0") Double amount) {

        return ResponseEntity.ok(transactionService.deposit(accountId, amount));
    }

    @Operation(summary = "Withdraw money from an account",
               description = "Deducts the specified amount from the given account's balance.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
        @ApiResponse(responseCode = "400", description = "Invalid account ID or insufficient balance")
    })
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(
            @Parameter(description = "Account ID from which money will be withdrawn")
            @RequestParam @NotBlank(message = "Account ID cannot be blank") String accountId,

            @Parameter(description = "Amount to withdraw (must be positive)")
            @RequestParam @NotNull(message = "Amount cannot be null") @Positive(message = "Amount must be greater than 0") Double amount) {

        return ResponseEntity.ok(transactionService.withdraw(accountId, amount));
    }

    @Operation(summary = "Get all transactions for an account",
               description = "Retrieves the list of all transactions linked to the given account ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactions(
            @Parameter(description = "Account ID to fetch transactions for")
            @PathVariable @NotBlank(message = "Account ID cannot be blank") String accountId) {

        return ResponseEntity.ok(transactionService.getTransactions(accountId));
    }
}
