package com.wipro.loan.controller;

import com.wipro.loan.entity.Loan;
import com.wipro.loan.service.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Operation(summary = "Apply for a new loan",
               description = "Submits a loan application with the provided loan details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Loan application submitted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid loan details")
    })
    @PostMapping("/apply")
    public ResponseEntity<Loan> applyLoan(
            @Parameter(description = "Loan details", required = true)
            @Valid @RequestBody @NotNull(message = "Loan details cannot be null") Loan loan) {
        return ResponseEntity.ok(loanService.applyLoan(loan));
    }

    @Operation(summary = "Repay a loan",
               description = "Repays a specific amount towards the loan with the given ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Loan repayment successful"),
        @ApiResponse(responseCode = "400", description = "Invalid repayment amount or loan ID"),
        @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @PostMapping("/{id}/repay")
    public ResponseEntity<Loan> repayLoan(
            @Parameter(description = "Loan ID") @PathVariable @NotNull(message = "Loan ID cannot be null") Long id,
            @Parameter(description = "Repayment amount (must be positive)")
            @RequestBody @NotNull(message = "Repayment amount cannot be null") @Positive(message = "Amount must be greater than 0") Double amount) {
        return ResponseEntity.ok(loanService.repayLoan(id, amount));
    }

    @Operation(summary = "Calculate loan interest",
               description = "Calculates the current interest for the given loan ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Interest calculated successfully"),
        @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @GetMapping("/{id}/interest")
    public ResponseEntity<Double> calculateInterest(
            @Parameter(description = "Loan ID") @PathVariable @NotNull(message = "Loan ID cannot be null") Long id) {
        return ResponseEntity.ok(loanService.calculateInterest(id));
    }

    @Operation(summary = "Close a loan",
               description = "Closes the loan with the given ID after full repayment.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Loan closed successfully"),
        @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @PostMapping("/{id}/close")
    public ResponseEntity<Loan> closeLoan(
            @Parameter(description = "Loan ID") @PathVariable @NotNull(message = "Loan ID cannot be null") Long id) {
        return ResponseEntity.ok(loanService.closeLoan(id));
    }
}
