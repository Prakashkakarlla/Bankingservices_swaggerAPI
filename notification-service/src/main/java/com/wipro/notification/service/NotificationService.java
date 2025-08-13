package com.wipro.notification.service;






import com.wipro.notification.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    private Long idCounter = 0L;

    public Notification sendNotification(Notification notification) throws MessagingException {
        if (notification.getRecipient() == null || notification.getRecipient().isEmpty()) {
            throw new IllegalArgumentException("Recipient email is required");
        }
        if (notification.getSubject() == null || notification.getSubject().isEmpty()) {
            throw new IllegalArgumentException("Subject is required");
        }
        if (notification.getBody() == null || notification.getBody().isEmpty()) {
            throw new IllegalArgumentException("Body is required");
        }
        notification.setId(++idCounter);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(notification.getRecipient());
        helper.setSubject(notification.getSubject());
        helper.setText(notification.getBody(), true);
        mailSender.send(message);
        notification.setStatus("SENT");
        return notification;
    }
}