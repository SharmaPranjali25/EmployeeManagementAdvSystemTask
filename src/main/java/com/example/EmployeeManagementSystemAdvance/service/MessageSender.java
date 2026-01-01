package com.example.EmployeeManagementSystemAdvance.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {
    
    private final JmsTemplate jmsTemplate;
    
    @Value("${jms.queue.name}")
    private String queueName;
    
    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    
    public void sendMessage(String message) {
        System.out.println("Sending message: " + message);
        jmsTemplate.convertAndSend(queueName, message);
        System.out.println("Message sent successfully!");
    }
}