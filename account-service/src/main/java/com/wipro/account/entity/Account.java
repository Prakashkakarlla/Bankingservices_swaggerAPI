package com.wipro.account.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    
    @NotBlank(message = "Account number cannot be blank")
    @Size(min = 6, max = 20, message = "Account number must be between 6 and 20 characters")
    private String accountNumber;

    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "SAVINGS|CHECKING|CURRENT", message = "Type must be SAVINGS, CHECKING, or CURRENT")
    private String type;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be negative")
    private Double balance;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "ACTIVE|INACTIVE|CLOSED", message = "Status must be ACTIVE, INACTIVE, or CLOSED")
    private String status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
}