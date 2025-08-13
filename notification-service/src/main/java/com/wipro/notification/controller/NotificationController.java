package com.wipro.notification.controller;

import com.wipro.notification.entity.Notification;
import com.wipro.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(summary = "Send a notification",
               description = "Sends a notification via email or other configured channels.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification sent successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid notification data"),
        @ApiResponse(responseCode = "500", description = "Error while sending notification")
    })
    @PostMapping
    public ResponseEntity<Notification> sendNotification(
            @Parameter(description = "Notification details to be sent", required = true)
            @Valid @RequestBody @NotNull(message = "Notification body cannot be null")
            Notification notification) throws MessagingException {

        return ResponseEntity.ok(notificationService.sendNotification(notification));
    }
}
