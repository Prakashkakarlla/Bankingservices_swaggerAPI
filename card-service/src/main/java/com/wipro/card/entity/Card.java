package com.wipro.card.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @NotBlank(message = "Account ID is required")
    @Size(min = 5, max = 50, message = "Account ID must be between 5 and 50 characters")
    private String accountId;

    @NotBlank(message = "Card number is required")
    @Pattern(
        regexp = "^[0-9]{16}$",
        message = "Card number must be exactly 16 digits"
    )
    private String cardNumber;

    @NotBlank(message = "Card type is required")
    @Pattern(
        regexp = "^(Debit|Credit)$",
        message = "Card type must be either 'Debit' or 'Credit'"
    )
    private String type;

    @NotBlank(message = "Status is required")
    @Pattern(
        regexp = "^(Active|Inactive|Blocked)$",
        message = "Status must be Active, Inactive, or Blocked"
    )
    private String status;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
}