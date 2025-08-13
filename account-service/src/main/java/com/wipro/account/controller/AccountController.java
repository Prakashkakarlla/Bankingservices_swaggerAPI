package com.wipro.account.controller;







import com.wipro.account.entity.Account;
import com.wipro.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/accounts")
@Validated
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Operation(summary = "Create a new account", description = "Creates an account with the specified type and balance")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @Operation(summary = "Update an account", description = "Updates the type of an existing account")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or closed account"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable @Min(value = 1, message = "ID must be positive") Long id, @Valid @RequestBody Account accountDetails) {
        return ResponseEntity.ok(accountService.updateAccount(id, accountDetails));
    }

    @Operation(summary = "Adjust account balance", description = "Adjusts the balance of an existing account")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Balance adjusted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid amount or insufficient funds"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/adjust")
    public ResponseEntity<Account> adjustBalance(@PathVariable @Min(value = 1, message = "ID must be positive") Long id, @RequestBody Double amount) {
        return ResponseEntity.ok(accountService.adjustBalance(id, amount));
    }

    @Operation(summary = "Get account balance", description = "Retrieves the balance of an account")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable @Min(value = 1, message = "ID must be positive") Long id) {
        return ResponseEntity.ok(accountService.getBalance(id));
    }

    @Operation(summary = "Close an account", description = "Closes an existing account")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account closed successfully"),
        @ApiResponse(responseCode = "400", description = "Account already closed"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/close")
    public ResponseEntity<Account> closeAccount(@PathVariable @Min(value = 1, message = "ID must be positive") Long id) {
        return ResponseEntity.ok(accountService.closeAccount(id));
    }
}