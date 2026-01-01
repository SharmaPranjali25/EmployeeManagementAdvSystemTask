package com.example.EmployeeManagementSystemAdvance.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {
    
    @JmsListener(destination = "${jms.queue.name}")
    public void receiveMessage(String message) {
        System.out.println("========================================");
        System.out.println("Received JMS Message: " + message);
        System.out.println("========================================");
    }
}