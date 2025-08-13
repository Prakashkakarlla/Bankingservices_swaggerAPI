package com.wipro.loan.event;




import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class NotificationEvent extends ApplicationEvent {
    private final String type;
    private final String accountId;
    private final String details;
    private final String recipientEmail;

    public NotificationEvent(Object source, String type, String accountId, String details, String recipientEmail) {
        super(source);
        this.type = type;
        this.accountId = accountId;
        this.details = details;
        this.recipientEmail = recipientEmail;
    }

	public String getType() {
		return type;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getDetails() {
		return details;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}
    
    
    
}