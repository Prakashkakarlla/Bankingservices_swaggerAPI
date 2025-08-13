package com.wipro.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.loan.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {
	
}