package com.wipro.loan.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.wipro.loan.entity.Loan;
import com.wipro.loan.event.NotificationEvent;
import com.wipro.loan.exception.ResourceNotFoundException;
import com.wipro.loan.repository.LoanRepository;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public Loan applyLoan(Loan loan) {
        if (loan.getAccountId() == null || loan.getAmount() == null || loan.getInterestRate() == null) {
            throw new IllegalArgumentException("Account ID, amount, and interest rate are required");
        }
        loan.setStatus("ACTIVE");
        Loan savedLoan = loanRepository.save(loan);
        eventPublisher.publishEvent(new NotificationEvent(this, "LOAN_APPLIED", loan.getAccountId(), "Loan applied: " + loan.getAmount(), "user@example.com"));
        return savedLoan;
    }

    @Transactional
    public Loan repayLoan(Long id, Double amount) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
        if (!loan.getStatus().equals("ACTIVE")) {
            throw new IllegalArgumentException("Cannot repay closed loan");
        }
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Repayment amount must be positive");
        }
        // Deduct from account balance
        String url = "http://account-service/api/accounts/" + loan.getAccountId() + "/adjust";
        restTemplate.postForObject(url, -amount, Void.class);
        loan.setAmount(loan.getAmount() - amount);
        if (loan.getAmount() <= 0) {
            loan.setAmount(0.0);
            loan.setStatus("CLOSED");
        }
        Loan updatedLoan = loanRepository.save(loan);
        eventPublisher.publishEvent(new NotificationEvent(this, "LOAN_REPAID", loan.getAccountId(), "Loan repaid by: " + amount, "user@example.com"));
        return updatedLoan;
    }

    public Double calculateInterest(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
        if (!loan.getStatus().equals("ACTIVE")) {
            throw new IllegalArgumentException("Cannot calculate interest for closed loan");
        }
        Double interest = loan.getAmount() * (loan.getInterestRate() / 100);
        eventPublisher.publishEvent(new NotificationEvent(this, "LOAN_INTEREST_CALCULATED", loan.getAccountId(), "Interest calculated: " + interest, "user@example.com"));
        return interest;
    }

    @Transactional
    public Loan closeLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
        if (!loan.getStatus().equals("ACTIVE")) {
            throw new IllegalArgumentException("Loan is already closed");
        }
        if (loan.getAmount() > 0) {
            throw new IllegalArgumentException("Cannot close loan with outstanding balance");
        }
        loan.setStatus("CLOSED");
        Loan closedLoan = loanRepository.save(loan);
        eventPublisher.publishEvent(new NotificationEvent(this, "LOAN_CLOSED", loan.getAccountId(), "Loan closed", "user@example.com"));
        return closedLoan;
    }
}

