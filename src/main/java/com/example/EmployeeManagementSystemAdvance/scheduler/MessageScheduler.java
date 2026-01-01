package com.example.EmployeeManagementSystemAdvance.scheduler;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.EmployeeManagementSystemAdvance.service.MessageSender;

import java.time.LocalDateTime;

@Component
public class MessageScheduler {
    
    private final MessageSender messageSender;
    private int counter = 0;
    
    public MessageScheduler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
    
    @Scheduled(fixedDelay = 5000) // Send message every 5 seconds
    public void sendScheduledMessage() {
        counter++;
        String message = "Scheduled message #" + counter + " at " + LocalDateTime.now();
        messageSender.sendMessage(message);
    }
}